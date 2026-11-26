import assert from 'node:assert/strict'
import test from 'node:test'
import { captureBody, classifyEvent, sanitize } from '../src/audit.js'

test('classifica o evento pelo método e status', () => {
  assert.equal(classifyEvent('GET', 200), 'CONSULTA')
  assert.equal(classifyEvent('POST', 201), 'CRIACAO')
  assert.equal(classifyEvent('PATCH', 400), 'REJEICAO')
  assert.equal(classifyEvent('DELETE', 500), 'ERRO')
})

test('remove segredos e mascara dados pessoais em qualquer nível', () => {
  assert.deepEqual(sanitize({
    senha: 'segredo',
    nested: { accessToken: 'abc', email: 'pessoa@teste.com', celular: '11912345678' },
  }), {
    senha: '[REDACTED]',
    nested: { accessToken: '[REDACTED]', email: 'pe***@teste.com', celular: '*******5678' },
  })
})

test('limita o corpo capturado', () => {
  const capture = captureBody(Buffer.from('texto muito longo'), 'text/plain', 5)
  assert.equal(capture.bytes, 17)
  assert.equal(capture.truncated, true)
  assert.deepEqual(capture.value, { contentType: 'text/plain', capturedBytes: 5 })
})
