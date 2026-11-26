export class ApiError extends Error {
  constructor(message, status = 0) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

async function parseError(response) {
  const body = await response.json().catch(() => null)
  if (body?.message) return body.message
  if (body?.detail) return body.detail
  if (response.status === 409) return 'Esse horário não está disponível para o barbeiro.'
  if (response.status === 400) return 'Revise os dados informados.'
  return `A API respondeu com o status ${response.status}.`
}

export function createApi(baseUrl) {
  async function request(path, options = {}) {
    const controller = new AbortController()
    const timeout = setTimeout(() => controller.abort(), 10_000)
    try {
      const response = await fetch(`${baseUrl}${path}`, {
        ...options,
        signal: controller.signal,
        headers: { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...options.headers },
      })
      if (!response.ok) throw new ApiError(await parseError(response), response.status)
      if (response.status === 204) return null
      const text = await response.text()
      return text ? JSON.parse(text) : null
    } catch (error) {
      if (error instanceof ApiError) throw error
      if (error.name === 'AbortError') throw new ApiError('A API demorou para responder.')
      throw new ApiError('Não foi possível conectar à API. Verifique se os servidores estão ativos.')
    } finally {
      clearTimeout(timeout)
    }
  }

  return {
    authenticate: (email, password) => request('/autenticacao/login', {
      method: 'POST',
      body: JSON.stringify({ email: email.trim(), senha: password }),
    }),
    barbers: () => request('/usuarios/barbeiros'),
    clients: () => request('/clientes'),
    services: () => request('/servicos?ativo=true'),
    createAppointment: (payload) => request('/agendamentos', { method: 'POST', body: JSON.stringify(payload) }),
  }
}
