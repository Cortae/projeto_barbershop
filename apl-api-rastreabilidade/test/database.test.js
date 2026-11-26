import assert from 'node:assert/strict'
import { readFile } from 'node:fs/promises'
import test from 'node:test'
import { createAuditRepository } from '../src/database.js'

test('persiste o ciclo completo usando fk_usuario', async () => {
  let statement
  let values
  const pool = {
    execute: async (sql, parameters) => {
      statement = sql
      values = parameters
    },
  }
  const repository = createAuditRepository(pool)
  const log = {
    requestId: '4a35991f-e7db-43e7-8dc1-92180321607f',
    startedAt: new Date('2026-10-24T10:00:00.000Z'),
    finishedAt: new Date('2026-10-24T10:00:00.025Z'),
    durationMs: 25,
    eventType: 'CRIACAO',
    httpMethod: 'POST',
    endpoint: '/clientes',
    queryParams: { origem: 'site' },
    requestHeaders: { authorization: '[REDACTED]' },
    requestBody: { senha: '[REDACTED]' },
    requestBodyBytes: 32,
    requestBodyTruncated: false,
    responseStatus: 201,
    responseHeaders: { 'content-type': 'application/json' },
    responseBody: { id: 10 },
    responseBodyBytes: 9,
    responseBodyTruncated: false,
    sourceIp: '127.0.0.1',
    userAgent: 'test',
    userId: 2,
    errorCode: null,
  }

  await repository.save(log)

  assert.match(statement, /INSERT INTO api_request_log/)
  assert.match(statement, /fk_usuario/)
  assert.doesNotMatch(statement, /user_id/)
  assert.equal(values.length, 21)
  assert.equal(values[19], 2)
  assert.equal(JSON.parse(values[7]).origem, 'site')
  assert.equal(JSON.parse(values[9]).senha, '[REDACTED]')
})

test('migration alinha PK e relacionamento com usuario', async () => {
  const migration = await readFile(new URL('../migrations/001_create_api_request_log.sql', import.meta.url), 'utf8')
  assert.match(migration, /id_api_request_log BIGINT UNSIGNED NOT NULL AUTO_INCREMENT/)
  assert.match(migration, /fk_usuario INT NULL/)
  assert.match(migration, /FOREIGN KEY \(fk_usuario\) REFERENCES usuario \(id_usuario\)/)
  assert.match(migration, /ON DELETE SET NULL/)
  assert.doesNotMatch(migration, /log_sistema/)
})

test('readiness confirma que a tabela de auditoria existe', async () => {
  let statement
  const repository = createAuditRepository({
    query: async (sql) => { statement = sql },
  })

  await repository.ping()

  assert.equal(statement, 'SELECT 1 FROM api_request_log LIMIT 1')
})
