const REPORT_API_URL = (import.meta.env.VITE_REPORT_API_URL || '/trace-api').replace(/\/$/, '')

export async function downloadMonthlyFinanceReport(month, summary) {
  const response = await fetch(`${REPORT_API_URL}/relatorios/financeiro/pdf`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ mes: month, resumo: summary }),
  })

  if (!response.ok) {
    const error = await response.json().catch(() => null)
    throw new Error(error?.message || 'Não foi possível gerar o relatório.')
  }

  const blob = await response.blob()
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `relatorio-financeiro-${month}.pdf`
  document.body.appendChild(link)
  link.click()
  link.remove()
  URL.revokeObjectURL(url)
}
