import { ApiError } from './apiError'

const API_BASE_URL = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

export async function apiRequest(path, options = {}) {
  const controller = new AbortController()
  const timeout = setTimeout(() => controller.abort(), options.timeout ?? 10_000)
  let response
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      signal: options.signal ?? controller.signal,
      headers: {
        ...(options.body ? { 'Content-Type': 'application/json' } : {}),
        ...options.headers,
      },
    })
  } catch (error) {
    if (error?.name === 'AbortError') {
      throw new ApiError('timeout', 'A API demorou para responder. Tente novamente.')
    }
    throw new ApiError('network', 'Não foi possível conectar à API. Verifique se o servidor está ativo.')
  } finally {
    clearTimeout(timeout)
  }

  if (!response.ok) {
    const error = await response.json().catch(() => null)
    const code = response.status === 409 ? 'conflict' : String(response.status)
    throw new ApiError(code, error?.message || error?.detail || 'Não foi possível concluir a solicitação.', error?.errors ?? [])
  }

  if (response.status === 204 || response.headers.get('content-length') === '0') return null
  const text = await response.text()
  return text ? JSON.parse(text) : null
}
