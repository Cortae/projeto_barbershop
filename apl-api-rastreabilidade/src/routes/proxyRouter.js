import { randomUUID } from 'node:crypto'
import { Router } from 'express'
import { captureBody, classifyEvent, sanitizeHeaders } from '../audit.js'

const REQUEST_ID_PATTERN = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i
const BLOCKED_REQUEST_HEADERS = new Set([
  'connection', 'content-length', 'cookie', 'host', 'keep-alive', 'proxy-authenticate',
  'proxy-authorization', 'te', 'trailer', 'transfer-encoding', 'upgrade',
])
const RESPONSE_HEADERS = new Set([
  'cache-control', 'content-disposition', 'content-language', 'content-type', 'etag',
  'expires', 'last-modified', 'location', 'vary',
])

export function createProxyRouter({ config, auditRepository }) {
  const router = Router()
  const proxy = (req, res) => forward(req, res, config, auditRepository)

  router.all('/{*path}', proxy)
  router.all('/', proxy)
  return router
}

async function forward(req, res, config, auditRepository) {
  const startedAt = new Date()
  const requestId = validRequestId(req.get('x-request-id'))
  const requestBody = Buffer.isBuffer(req.body) ? req.body : Buffer.alloc(0)
  const target = new URL(req.originalUrl, `${config.springApiUrl}/`)
  const controller = new AbortController()
  const timeout = setTimeout(() => controller.abort(), config.upstreamTimeoutMs)

  try {
    const upstream = await fetch(target, {
      method: req.method,
      headers: requestHeaders(req.headers, requestId),
      body: allowsBody(req.method) && requestBody.length ? requestBody : undefined,
      signal: controller.signal,
      redirect: 'manual',
    })
    const responseBody = Buffer.from(await upstream.arrayBuffer())
    const responseHeaders = selectedResponseHeaders(upstream.headers)
    const log = buildAuditLog({
      req, requestId, startedAt, requestBody, responseBody,
      status: upstream.status, responseHeaders, config,
    })

    if (!(await persistAudit(log, auditRepository, config.auditFailureMode))) {
      return gatewayResponse(res, 503, 'AUDIT_UNAVAILABLE', 'Nao foi possivel registrar a auditoria.', requestId)
    }

    res.status(upstream.status).set(responseHeaders).set('X-Request-Id', requestId)
    return responseBody.length ? res.send(responseBody) : res.end()
  } catch (error) {
    const timedOut = error?.name === 'AbortError'
    const status = timedOut ? 504 : 502
    const code = timedOut ? 'UPSTREAM_TIMEOUT' : 'UPSTREAM_UNAVAILABLE'
    const message = timedOut
      ? 'O servico principal demorou para responder.'
      : 'Nao foi possivel conectar ao servico principal.'
    const responseBody = errorBody(status, code, message, requestId)
    const log = buildAuditLog({
      req, requestId, startedAt, requestBody, responseBody,
      status, responseHeaders: { 'content-type': 'application/json; charset=utf-8' }, config, errorCode: code,
    })

    if (!(await persistAudit(log, auditRepository, config.auditFailureMode))) {
      return gatewayResponse(res, 503, 'AUDIT_UNAVAILABLE', 'Nao foi possivel registrar a auditoria.', requestId)
    }
    return res.status(status).type('json').set('X-Request-Id', requestId).send(responseBody)
  } finally {
    clearTimeout(timeout)
  }
}

function validRequestId(value) {
  return value && REQUEST_ID_PATTERN.test(value) ? value : randomUUID()
}

function allowsBody(method) {
  return method !== 'GET' && method !== 'HEAD'
}

function requestHeaders(headers, requestId) {
  const forwarded = new Headers()
  for (const [name, value] of Object.entries(headers)) {
    if (value != null && !BLOCKED_REQUEST_HEADERS.has(name.toLowerCase())) {
      forwarded.set(name, Array.isArray(value) ? value.join(', ') : value)
    }
  }
  forwarded.set('x-request-id', requestId)
  return forwarded
}

function selectedResponseHeaders(headers) {
  const selected = {}
  for (const [name, value] of headers.entries()) {
    if (RESPONSE_HEADERS.has(name.toLowerCase())) selected[name] = value
  }
  return selected
}

function buildAuditLog({ req, requestId, startedAt, requestBody, responseBody, status, responseHeaders, config, errorCode = null }) {
  const finishedAt = new Date()
  const requestCapture = captureBody(requestBody, req.get('content-type'), config.auditBodyMaxBytes)
  const responseCapture = captureBody(responseBody, responseHeaders['content-type'], config.auditBodyMaxBytes)
  const url = new URL(req.originalUrl, 'http://gateway.local')
  return {
    requestId,
    startedAt,
    finishedAt,
    durationMs: Math.max(0, finishedAt.getTime() - startedAt.getTime()),
    eventType: classifyEvent(req.method, status),
    httpMethod: req.method,
    endpoint: url.pathname,
    queryParams: queryParams(url.searchParams),
    requestHeaders: sanitizeHeaders(pickHeaders(req.headers, ['accept', 'accept-language', 'authorization', 'content-type', 'x-request-id'])),
    requestBody: requestCapture.value,
    requestBodyBytes: requestCapture.bytes,
    requestBodyTruncated: requestCapture.truncated,
    responseStatus: status,
    responseHeaders: sanitizeHeaders(responseHeaders),
    responseBody: responseCapture.value,
    responseBodyBytes: responseCapture.bytes,
    responseBodyTruncated: responseCapture.truncated,
    sourceIp: req.ip,
    userAgent: req.get('user-agent') || null,
    userId: config.allowDevUserHeader ? positiveInteger(req.get('x-user-id')) : null,
    errorCode,
  }
}

function queryParams(searchParams) {
  const result = {}
  for (const [key, value] of searchParams) {
    if (Object.hasOwn(result, key)) result[key] = Array.isArray(result[key]) ? [...result[key], value] : [result[key], value]
    else result[key] = value
  }
  return result
}

function pickHeaders(headers, names) {
  return Object.fromEntries(names.filter((name) => headers[name] != null).map((name) => [name, headers[name]]))
}

function positiveInteger(value) {
  const number = Number(value)
  return Number.isSafeInteger(number) && number > 0 ? number : null
}

async function persistAudit(log, repository, failureMode) {
  try {
    await repository.save(log)
    return true
  } catch (error) {
    console.error(JSON.stringify({ level: 'error', event: 'audit_failed', requestId: log.requestId, message: error?.message }))
    return failureMode === 'open'
  }
}

function errorBody(status, code, message, requestId) {
  return Buffer.from(JSON.stringify({ timestamp: new Date().toISOString(), status, code, message, requestId }))
}

function gatewayResponse(res, status, code, message, requestId) {
  return res.status(status).set('X-Request-Id', requestId).json({
    timestamp: new Date().toISOString(), status, code, message, requestId,
  })
}
