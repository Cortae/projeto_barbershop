import puppeteer from 'puppeteer'
import {
  buildAuditLog,
  errorBody,
  gatewayResponse,
  persistAudit,
  validRequestId,
} from '../routes/proxyRouter.js'

const MONTH_PATTERN = /^\d{4}-(0[1-9]|1[0-2])$/

export function createFinanceReportHandler({ config, auditRepository, browserLauncher = (options) => puppeteer.launch(options) }) {
  return async function financeReportHandler(req, res) {
    const startedAt = new Date()
    const requestId = validRequestId(req.get('x-request-id'))
    const requestBody = Buffer.isBuffer(req.body) ? req.body : Buffer.alloc(0)
    let result

    try {
      result = await generateFinanceReport(requestBody, browserLauncher)
    } catch (error) {
      console.error(error)
      result = {
        status: 500,
        headers: { 'content-type': 'application/json; charset=utf-8' },
        body: errorBody(500, 'REPORT_GENERATION_FAILED', 'Nao foi possivel gerar o relatorio.', requestId),
        errorCode: 'REPORT_GENERATION_FAILED',
      }
    }

    const log = buildAuditLog({
      req,
      requestId,
      startedAt,
      requestBody,
      responseBody: result.body,
      status: result.status,
      responseHeaders: result.headers,
      config,
      errorCode: result.errorCode,
    })

    if (!(await persistAudit(log, auditRepository, config.auditFailureMode))) {
      return gatewayResponse(res, 503, 'AUDIT_UNAVAILABLE', 'Nao foi possivel registrar a auditoria.', requestId)
    }

    return res.status(result.status).set(result.headers).set('X-Request-Id', requestId).send(result.body)
  }
}

async function generateFinanceReport(requestBody, browserLauncher) {
  if (!requestBody.length) return jsonResponse(400, { message: 'Dados do relat\u00f3rio inv\u00e1lidos.' })

  let payload
  try {
    payload = JSON.parse(requestBody.toString('utf8'))
  } catch {
    return jsonResponse(400, { message: 'O corpo deve ser um JSON v\u00e1lido.' })
  }

  const validationError = validate(payload)
  if (validationError) return jsonResponse(400, { message: validationError })

  let browser
  try {
    browser = await browserLauncher({
      headless: true,
      pipe: true,
      timeout: 60_000,
      args: [
        '--no-sandbox',
        '--disable-setuid-sandbox',
        '--disable-dev-shm-usage',
        '--no-first-run',
        '--no-default-browser-check',
      ],
    })
    const page = await browser.newPage()
    await page.setContent(renderReport(payload), { waitUntil: 'domcontentloaded' })
    const pdf = await page.pdf({
      format: 'A4',
      printBackground: true,
      margin: { top: '16mm', right: '14mm', bottom: '16mm', left: '14mm' },
    })

    return {
      status: 200,
      headers: {
        'content-type': 'application/pdf',
        'content-disposition': `attachment; filename="relatorio-financeiro-${payload.mes}.pdf"`,
        'content-length': String(pdf.length),
        'cache-control': 'no-store',
      },
      body: Buffer.from(pdf),
      errorCode: null,
    }
  } finally {
    await browser?.close()
  }
}

function jsonResponse(status, value) {
  return {
    status,
    headers: { 'content-type': 'application/json; charset=utf-8' },
    body: Buffer.from(JSON.stringify(value)),
    errorCode: status >= 400 ? 'INVALID_REPORT_REQUEST' : null,
  }
}

function validate(payload) {
  if (!payload || !MONTH_PATTERN.test(payload.mes)) return 'Informe um m\u00eas v\u00e1lido no formato AAAA-MM.'
  if (!payload.resumo || typeof payload.resumo !== 'object') return 'Informe o resumo financeiro.'
  const fields = ['valorBruto', 'despesas', 'valorLiquido', 'valorAReceber']
  if (fields.some((field) => !Number.isFinite(Number(payload.resumo[field])))) {
    return 'O resumo financeiro cont\u00e9m valores inv\u00e1lidos.'
  }
  return null
}

function renderReport({ mes, resumo }) {
  const [year, month] = mes.split('-').map(Number)
  const monthLabel = new Intl.DateTimeFormat('pt-BR', { month: 'long', year: 'numeric', timeZone: 'UTC' })
    .format(new Date(Date.UTC(year, month - 1, 1)))
  const generatedAt = new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short', timeStyle: 'short', timeZone: 'America/Sao_Paulo',
  }).format(new Date())
  const weeks = Array.isArray(resumo.semanas) ? resumo.semanas : []
  const services = Array.isArray(resumo.porServico) ? resumo.porServico : []
  const pending = Array.isArray(resumo.pendentes) ? resumo.pendentes : []

  return `<!doctype html>
  <html lang="pt-BR"><head><meta charset="utf-8"><style>
    *{box-sizing:border-box} body{font-family:Arial,sans-serif;color:#18181b;font-size:11px;margin:0}
    h1{font-size:23px;margin:0 0 4px} h2{font-size:15px;margin:24px 0 8px;color:#166534}
    .header{border-bottom:3px solid #22c55e;padding-bottom:12px}.muted{color:#71717a}.capitalize{text-transform:capitalize}
    .kpis{display:grid;grid-template-columns:repeat(4,1fr);gap:8px;margin-top:18px}.kpi{border:1px solid #d4d4d8;border-radius:7px;padding:10px}
    .kpi span{display:block;color:#52525b;margin-bottom:5px}.kpi strong{font-size:15px}.positive{color:#15803d}.negative{color:#b91c1c}
    table{width:100%;border-collapse:collapse}th,td{padding:7px 8px;border-bottom:1px solid #e4e4e7;text-align:left}th{background:#f4f4f5;color:#3f3f46}
    .number{text-align:right;font-variant-numeric:tabular-nums}.empty{padding:12px;background:#f4f4f5;color:#71717a;border-radius:6px}
    footer{margin-top:28px;padding-top:8px;border-top:1px solid #d4d4d8;color:#71717a;font-size:9px}
  </style></head><body>
    <header class="header"><h1>Relat\u00f3rio financeiro mensal</h1><div class="muted capitalize">${escapeHtml(monthLabel)}</div></header>
    <section class="kpis">
      ${kpi('Receita bruta', resumo.valorBruto)}${kpi('Despesas', resumo.despesas, 'negative')}
      ${kpi('Resultado l\u00edquido', resumo.valorLiquido, Number(resumo.valorLiquido) >= 0 ? 'positive' : 'negative')}${kpi('A receber', resumo.valorAReceber)}
    </section>
    ${tableSection('Resumo por semana', ['Per\u00edodo', 'Receita', 'Despesas', 'Resultado'], weeks.map((week) => [
      week.rotulo, money(week.valorBruto), money(week.despesas), money(Number(week.valorBruto) - Number(week.despesas)),
    ]), [1, 2, 3])}
    ${tableSection('Receita por servi\u00e7o', ['Servi\u00e7o', 'Participa\u00e7\u00e3o', 'Receita'], services.map((service) => [
      service.nome, `${Number(service.percentual) || 0}%`, money(service.total),
    ]), [1, 2])}
    ${tableSection('Pagamentos pendentes', ['Cliente', 'Data', 'Valor'], pending.map((item) => [
      item.cliente?.nome || item.clienteNome || 'Cliente', dateTime(item.horarioInicio), money(item.valor),
    ]), [2])}
    <footer>JR Barbershop \u2022 Documento gerado em ${escapeHtml(generatedAt)}</footer>
  </body></html>`
}

function kpi(label, value, className = '') {
  return `<div class="kpi"><span>${label}</span><strong class="${className}">${money(value)}</strong></div>`
}

function tableSection(title, headers, rows, numericColumns = []) {
  if (!rows.length) return `<section><h2>${title}</h2><div class="empty">Nenhum registro no per\u00edodo.</div></section>`
  return `<section><h2>${title}</h2><table><thead><tr>${headers.map((header, index) => `<th class="${numericColumns.includes(index) ? 'number' : ''}">${header}</th>`).join('')}</tr></thead><tbody>${rows.map((row) => `<tr>${row.map((cell, index) => `<td class="${numericColumns.includes(index) ? 'number' : ''}">${escapeHtml(cell)}</td>`).join('')}</tr>`).join('')}</tbody></table></section>`
}

function money(value) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(value) || 0)
}

function dateTime(value) {
  if (!value) return '-'
  const date = new Date(value)
  return Number.isNaN(date.getTime()) ? '-' : new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(date)
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, (char) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' })[char])
}
