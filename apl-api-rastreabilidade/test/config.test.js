import assert from 'node:assert/strict'
import test from 'node:test'
import { loadConfig } from '../src/config.js'

test('carrega os valores padrão', () => {
  const config = loadConfig({})
  assert.equal(config.port, 3000)
  assert.equal(config.springApiUrl, 'http://localhost:8080')
  assert.equal(config.auditFailureMode, 'open')
})

test('impede cabeçalho de usuário de desenvolvimento em produção', () => {
  assert.throws(
    () => loadConfig({ NODE_ENV: 'production', ALLOW_DEV_USER_HEADER: 'true' }),
    /não pode ser habilitado em produção/,
  )
})
