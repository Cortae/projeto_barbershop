const TRUE_VALUES = new Set(['true', '1', 'yes'])

function integer(name, value, fallback, { min = 1, max = Number.MAX_SAFE_INTEGER } = {}) {
  const parsed = Number.parseInt(value ?? String(fallback), 10)
  if (!Number.isInteger(parsed) || parsed < min || parsed > max) {
    throw new Error(`${name} deve ser um inteiro entre ${min} e ${max}`)
  }
  return parsed
}

function boolean(value, fallback = false) {
  return value == null ? fallback : TRUE_VALUES.has(value.toLowerCase())
}

function upstreamUrl(value) {
  const url = new URL(value || 'http://localhost:8080')
  if (!['http:', 'https:'].includes(url.protocol) || url.username || url.password) {
    throw new Error('SPRING_API_URL deve ser uma URL HTTP(S) sem credenciais')
  }
  return url.toString().replace(/\/$/, '')
}

export function loadConfig(env = process.env) {
  const nodeEnv = env.NODE_ENV || 'development'
  const failureMode = env.AUDIT_FAILURE_MODE || 'open'
  const allowDevUserHeader = boolean(env.ALLOW_DEV_USER_HEADER, false)

  if (!['open', 'closed'].includes(failureMode)) {
    throw new Error('AUDIT_FAILURE_MODE deve ser open ou closed')
  }
  if (nodeEnv === 'production' && allowDevUserHeader) {
    throw new Error('ALLOW_DEV_USER_HEADER não pode ser habilitado em produção')
  }

  return {
    nodeEnv,
    port: integer('PORT', env.PORT, 3000, { max: 65535 }),
    springApiUrl: upstreamUrl(env.SPRING_API_URL),
    upstreamTimeoutMs: integer('UPSTREAM_TIMEOUT_MS', env.UPSTREAM_TIMEOUT_MS, 10_000),
    requestBodyLimit: env.REQUEST_BODY_LIMIT || '1mb',
    auditFailureMode: failureMode,
    auditBodyMaxBytes: integer('AUDIT_BODY_MAX_BYTES', env.AUDIT_BODY_MAX_BYTES, 65_536),
    corsOrigins: (env.CORS_ORIGINS || '').split(',').map((item) => item.trim()).filter(Boolean),
    trustProxy: boolean(env.TRUST_PROXY, false),
    allowDevUserHeader,
    database: {
      host: env.DB_HOST || 'localhost',
      port: integer('DB_PORT', env.DB_PORT, 3306, { max: 65535 }),
      database: env.DB_NAME || 'db_barbershop',
      user: env.DB_USER || 'barbershop_logger',
      password: env.DB_PASSWORD || '',
      connectionLimit: integer('DB_POOL_SIZE', env.DB_POOL_SIZE, 10, { max: 100 }),
      connectTimeout: integer('DB_CONNECT_TIMEOUT_MS', env.DB_CONNECT_TIMEOUT_MS, 3000),
    },
  }
}
