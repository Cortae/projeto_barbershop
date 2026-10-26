import 'dotenv/config'
import { createApp } from './app.js'
import { loadConfig } from './config.js'
import { createAuditRepository, createDatabasePool } from './database.js'

const config = loadConfig()
const pool = createDatabasePool(config.database)
const auditRepository = createAuditRepository(pool)
const app = createApp({ config, auditRepository })
const server = app.listen(config.port, () => {
  console.log(JSON.stringify({ level: 'info', event: 'server_started', port: config.port, upstream: config.springApiUrl }))
})

async function shutdown(signal) {
  console.log(JSON.stringify({ level: 'info', event: 'server_stopping', signal }))
  server.close(async () => {
    await pool.end()
    process.exit(0)
  })
  setTimeout(() => process.exit(1), 10_000).unref()
}

process.on('SIGINT', () => shutdown('SIGINT'))
process.on('SIGTERM', () => shutdown('SIGTERM'))
