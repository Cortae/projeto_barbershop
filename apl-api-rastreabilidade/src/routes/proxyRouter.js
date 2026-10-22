import { randomUUID } from 'node:crypto'
import { Router } from 'express'

// Ponto de extensão da futura integração com o Spring Boot.
// Por enquanto, nenhum endpoint de negócio é encaminhado.
export function createProxyRouter() {
  const router = Router()

  router.all('/{*path}', (req, res) => notImplemented(req, res))
  router.all('/', (req, res) => notImplemented(req, res))

  return router
}

function notImplemented(req, res) {
  const requestId = randomUUID()
  res.set('X-Request-Id', requestId)
  res.status(501).json({
    timestamp: new Date().toISOString(),
    status: 501,
    code: 'PROXY_NOT_IMPLEMENTED',
    message: 'O encaminhamento para o backend Spring ainda não foi implementado.',
    method: req.method,
    path: req.originalUrl,
    requestId,
  })
}
