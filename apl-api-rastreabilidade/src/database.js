import mysql from 'mysql2/promise'

export function createDatabasePool(config) {
  return mysql.createPool({
    ...config,
    waitForConnections: true,
    enableKeepAlive: true,
    timezone: 'Z',
    charset: 'utf8mb4',
  })
}

export function createAuditRepository(pool) {
  return {
    async save(log) {
      await pool.execute(
        `INSERT INTO api_request_log (
          request_id, started_at, finished_at, duration_ms, event_type,
          http_method, endpoint, query_params, request_headers, request_body,
          request_body_bytes, request_body_truncated, response_status,
          response_headers, response_body, response_body_bytes,
          response_body_truncated, source_ip, user_agent, user_id, error_code
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
        [
          log.requestId, log.startedAt, log.finishedAt, log.durationMs, log.eventType,
          log.httpMethod, log.endpoint, json(log.queryParams), json(log.requestHeaders),
          json(log.requestBody), log.requestBodyBytes, log.requestBodyTruncated,
          log.responseStatus, json(log.responseHeaders), json(log.responseBody),
          log.responseBodyBytes, log.responseBodyTruncated, log.sourceIp,
          log.userAgent, log.userId, log.errorCode,
        ],
      )
    },
    async ping() {
      await pool.query('SELECT 1')
    },
  }
}

function json(value) {
  return value == null ? null : JSON.stringify(value)
}
