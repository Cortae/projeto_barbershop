import express from 'express'
import { createProxyRouter } from './routes/proxyRouter.js'

export function createApp({ config, auditRepository }) {
  const app = express()
  app.disable('x-powered-by')
  app.set('trust proxy', config.trustProxy)
  app.use(cors(config.corsOrigins))

  app.get('/health/live', (_req, res) => res.json({ status: 'UP' }))
  app.get('/health/ready', async (_req, res) => {
    try {
      await auditRepository.ping()
      res.json({ status: 'UP', database: 'UP' })
    } catch {
      res.status(503).json({ status: 'DOWN', database: 'DOWN' })
    }
  })

  app.use(express.raw({ type: '*/*', limit: config.requestBodyLimit }))
  app.use(createProxyRouter({ config, auditRepository }))

  app.use((error, _req, res, _next) => {
    if (error?.type === 'entity.too.large') {
      return res.status(413).json({ status: 413, code: 'PAYLOAD_TOO_LARGE', message: 'O corpo da requisição excede o limite permitido.' })
    }
    console.error(error)
    res.status(500).json({ status: 500, code: 'INTERNAL_ERROR', message: 'Erro interno na API intermediária.' })
  })
  return app
}

function cors(origins) {
  return function corsMiddleware(req, res, next) {
    const origin = req.get('origin')
    if (origin && origins.includes(origin)) {
      res.set({
        'Access-Control-Allow-Origin': origin,
        Vary: 'Origin',
        'Access-Control-Allow-Headers': req.get('access-control-request-headers') || 'Authorization, Content-Type, X-Request-Id, X-User-Id',
        'Access-Control-Allow-Methods': 'GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS',
        'Access-Control-Expose-Headers': 'X-Request-Id, Location, ETag',
      })
    }
    if (req.method === 'OPTIONS') return res.sendStatus(204)
    next()
  }
}
