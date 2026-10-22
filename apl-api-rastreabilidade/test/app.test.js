import assert from 'node:assert/strict'
import test from 'node:test'
import { createApp } from '../src/app.js'
import { loadConfig } from '../src/config.js'

test('expõe health live e mantém o proxy como não implementado', async (context) => {
  const auditRepository = { ping: async () => {} }
  const server = createApp({ config: loadConfig({}), auditRepository }).listen(0)
  context.after(() => server.close())

  await new Promise((resolve) => server.once('listening', resolve))
  const { port } = server.address()

  const health = await fetch(`http://127.0.0.1:${port}/health/live`)
  assert.equal(health.status, 200)
  assert.deepEqual(await health.json(), { status: 'UP' })

  const proxy = await fetch(`http://127.0.0.1:${port}/api/clientes`)
  const body = await proxy.json()
  assert.equal(proxy.status, 501)
  assert.equal(body.code, 'PROXY_NOT_IMPLEMENTED')
  assert.equal(proxy.headers.get('x-request-id'), body.requestId)
})
