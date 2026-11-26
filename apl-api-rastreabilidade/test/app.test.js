import assert from 'node:assert/strict'
import { createServer } from 'node:http'
import test from 'node:test'
import { createApp } from '../src/app.js'
import { loadConfig } from '../src/config.js'

const listen = async (server) => {
  server.listen(0, '127.0.0.1')
  await new Promise((resolve) => server.once('listening', resolve))
  return `http://127.0.0.1:${server.address().port}`
}

const repository = (logs) => ({
  ping: async () => {},
  save: async (log) => logs.push(log),
})

test('preserva metodo, path, query, corpo e sanitiza o ciclo auditado', async (context) => {
  let received
  const spring = createServer(async (req, res) => {
    const chunks = []
    for await (const chunk of req) chunks.push(chunk)
    received = {
      method: req.method,
      url: req.url,
      body: Buffer.concat(chunks).toString(),
      requestId: req.headers['x-request-id'],
    }
    res.writeHead(201, { 'content-type': 'application/json', etag: 'teste' })
    res.end(JSON.stringify({ senha: 'resposta-secreta', ok: true }))
  })
  const springUrl = await listen(spring)
  context.after(() => spring.close())

  const logs = []
  const gateway = createApp({
    config: loadConfig({ SPRING_API_URL: springUrl, ALLOW_DEV_USER_HEADER: 'true' }),
    auditRepository: repository(logs),
  }).listen(0, '127.0.0.1')
  context.after(() => gateway.close())
  await new Promise((resolve) => gateway.once('listening', resolve))
  const gatewayUrl = `http://127.0.0.1:${gateway.address().port}`

  const health = await fetch(`${gatewayUrl}/health/live`)
  assert.deepEqual(await health.json(), { status: 'UP' })

  const body = JSON.stringify({ nome: 'Ana', senha: 'segredo', nested: { accessToken: 'abc' } })
  const response = await fetch(`${gatewayUrl}/clientes?ativo=true&tag=a&tag=b`, {
    method: 'POST',
    headers: { 'content-type': 'application/json', authorization: 'Bearer segredo', 'x-user-id': '2' },
    body,
  })

  assert.equal(response.status, 201)
  assert.deepEqual(await response.json(), { senha: 'resposta-secreta', ok: true })
  assert.equal(response.headers.get('etag'), 'teste')
  assert.equal(response.headers.get('x-request-id'), received.requestId)
  assert.deepEqual(received, {
    method: 'POST',
    url: '/clientes?ativo=true&tag=a&tag=b',
    body,
    requestId: response.headers.get('x-request-id'),
  })

  assert.equal(logs.length, 1)
  assert.equal(logs[0].endpoint, '/clientes')
  assert.deepEqual(logs[0].queryParams, { ativo: 'true', tag: ['a', 'b'] })
  assert.equal(logs[0].requestHeaders.authorization, '[REDACTED]')
  assert.equal(logs[0].requestBody.senha, '[REDACTED]')
  assert.equal(logs[0].requestBody.nested.accessToken, '[REDACTED]')
  assert.equal(logs[0].responseBody.senha, '[REDACTED]')
  assert.equal(logs[0].userId, 2)
})

test('modo intermediado equivale ao direto para 204, 4xx, 5xx e texto', async (context) => {
  const spring = createServer((req, res) => {
    if (req.url === '/empty') return res.writeHead(204).end()
    if (req.url === '/domain-error') {
      res.writeHead(422, { 'content-type': 'application/json' })
      return res.end(JSON.stringify({ message: 'regra de dominio' }))
    }
    if (req.url === '/server-error') {
      res.writeHead(500, { 'content-type': 'text/plain' })
      return res.end('falha interna')
    }
    res.writeHead(200, { 'content-type': 'text/plain' })
    return res.end('conteudo nao JSON')
  })
  const springUrl = await listen(spring)
  context.after(() => spring.close())
  const logs = []
  const gateway = createApp({
    config: loadConfig({ SPRING_API_URL: springUrl }),
    auditRepository: repository(logs),
  }).listen(0, '127.0.0.1')
  context.after(() => gateway.close())
  await new Promise((resolve) => gateway.once('listening', resolve))
  const gatewayUrl = `http://127.0.0.1:${gateway.address().port}`

  for (const path of ['/empty', '/domain-error', '/server-error', '/text']) {
    const direct = await fetch(`${springUrl}${path}`)
    const directBody = await direct.text()
    assert.equal(logs.length, ['/empty', '/domain-error', '/server-error', '/text'].indexOf(path))

    const traced = await fetch(`${gatewayUrl}${path}`)
    assert.equal(traced.status, direct.status)
    assert.equal(await traced.text(), directBody)
  }
  assert.equal(logs.length, 4)
  assert.deepEqual(logs.map((log) => log.responseStatus), [204, 422, 500, 200])
})

test('diferencia timeout de indisponibilidade e audita ambos', async (context) => {
  const slowSpring = createServer(() => {})
  const slowUrl = await listen(slowSpring)
  context.after(() => {
    slowSpring.closeAllConnections()
    slowSpring.close()
  })
  const timeoutLogs = []
  const timeoutGateway = createApp({
    config: loadConfig({ SPRING_API_URL: slowUrl, UPSTREAM_TIMEOUT_MS: '30' }),
    auditRepository: repository(timeoutLogs),
  }).listen(0, '127.0.0.1')
  context.after(() => timeoutGateway.close())
  await new Promise((resolve) => timeoutGateway.once('listening', resolve))

  const timeout = await fetch(`http://127.0.0.1:${timeoutGateway.address().port}/clientes`)
  assert.equal(timeout.status, 504)
  assert.equal((await timeout.json()).code, 'UPSTREAM_TIMEOUT')
  assert.equal(timeoutLogs.length, 1)
  assert.equal(timeoutLogs[0].errorCode, 'UPSTREAM_TIMEOUT')

  const unavailableLogs = []
  const unavailableGateway = createApp({
    config: loadConfig({ SPRING_API_URL: 'http://127.0.0.1:1' }),
    auditRepository: repository(unavailableLogs),
  }).listen(0, '127.0.0.1')
  context.after(() => unavailableGateway.close())
  await new Promise((resolve) => unavailableGateway.once('listening', resolve))

  const unavailable = await fetch(`http://127.0.0.1:${unavailableGateway.address().port}/clientes`)
  assert.equal(unavailable.status, 502)
  assert.equal((await unavailable.json()).code, 'UPSTREAM_UNAVAILABLE')
  assert.equal(unavailableLogs.length, 1)
  assert.equal(unavailableLogs[0].errorCode, 'UPSTREAM_UNAVAILABLE')
})

test('politica closed devolve 503 quando a auditoria falha', async (context) => {
  const spring = createServer((_req, res) => res.end('{}'))
  const springUrl = await listen(spring)
  context.after(() => spring.close())
  const gateway = createApp({
    config: loadConfig({ SPRING_API_URL: springUrl, AUDIT_FAILURE_MODE: 'closed' }),
    auditRepository: { ping: async () => {}, save: async () => { throw new Error('banco indisponivel') } },
  }).listen(0, '127.0.0.1')
  context.after(() => gateway.close())
  await new Promise((resolve) => gateway.once('listening', resolve))

  const response = await fetch(`http://127.0.0.1:${gateway.address().port}/clientes`)
  assert.equal(response.status, 503)
  assert.equal((await response.json()).code, 'AUDIT_UNAVAILABLE')
})

test('valida o endpoint proprio do relatorio antes do proxy', async (context) => {
  const logs = []
  const gateway = createApp({
    config: loadConfig({ SPRING_API_URL: 'http://127.0.0.1:1' }),
    auditRepository: repository(logs),
  }).listen(0, '127.0.0.1')
  context.after(() => gateway.close())
  await new Promise((resolve) => gateway.once('listening', resolve))

  const response = await fetch(`http://127.0.0.1:${gateway.address().port}/relatorios/financeiro/pdf`, {
    method: 'POST',
    headers: { 'content-type': 'application/json' },
    body: JSON.stringify({ mes: 'invalido', resumo: {} }),
  })

  assert.equal(response.status, 400)
  assert.match((await response.json()).message, /m.s v.lido/i)
  assert.equal(logs.length, 0)
})
