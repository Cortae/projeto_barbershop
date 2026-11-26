import { apiRequest } from '@/shared/api'

export const signIn = ({ email, senha }) => apiRequest('/autenticacao/login', {
  method: 'POST',
  body: JSON.stringify({ email: email.trim(), senha }),
})

export const recoverPassword = (email) => apiRequest('/autenticacao/recuperar-senha', {
  method: 'POST',
  body: JSON.stringify({ email: email.trim() }),
})
