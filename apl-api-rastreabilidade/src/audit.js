const SENSITIVE_KEYS = /^(authorization|cookie|set-cookie|senha|password|passwd|token|access[-_]?token|refresh[-_]?token|google[-_]?sub|secret|api[-_]?key)$/i
const PERSONAL_KEYS = /^(email|e-mail|celular|telefone|phone)$/i

export function classifyEvent(method, status) {
  if (status >= 500) return 'ERRO'
  if (status >= 400) return 'REJEICAO'
  return ({ GET: 'CONSULTA', POST: 'CRIACAO', PUT: 'ATUALIZACAO', PATCH: 'ATUALIZACAO', DELETE: 'EXCLUSAO' })[method] || 'OUTRO'
}

export function sanitize(value, seen = new WeakSet()) {
  if (value == null || typeof value !== 'object') return value
  if (seen.has(value)) return '[Circular]'
  seen.add(value)
  if (Array.isArray(value)) return value.map((item) => sanitize(item, seen))

  return Object.fromEntries(Object.entries(value).map(([key, item]) => {
    if (SENSITIVE_KEYS.test(key)) return [key, '[REDACTED]']
    if (PERSONAL_KEYS.test(key)) return [key, maskPersonal(item)]
    return [key, sanitize(item, seen)]
  }))
}

export function sanitizeHeaders(headers) {
  const object = headers instanceof Headers ? Object.fromEntries(headers.entries()) : headers
  return sanitize(object || {})
}

export function captureBody(buffer, contentType, maxBytes) {
  const bytes = buffer?.length || 0
  if (!bytes) return { value: null, bytes: 0, truncated: false }

  const truncated = bytes > maxBytes
  const slice = buffer.subarray(0, maxBytes)
  if (!/[/+]json(?:;|$)/i.test(contentType || '')) {
    return { value: { contentType: contentType || null, capturedBytes: slice.length }, bytes, truncated }
  }

  try {
    return { value: sanitize(JSON.parse(slice.toString('utf8'))), bytes, truncated }
  } catch {
    return { value: { invalidJson: true, capturedBytes: slice.length }, bytes, truncated }
  }
}

function maskPersonal(value) {
  if (typeof value !== 'string' || value.length < 4) return '[MASKED]'
  if (value.includes('@')) {
    const [name, domain] = value.split('@')
    return `${name.slice(0, 2)}***@${domain || '***'}`
  }
  return `${'*'.repeat(Math.max(0, value.length - 4))}${value.slice(-4)}`
}
