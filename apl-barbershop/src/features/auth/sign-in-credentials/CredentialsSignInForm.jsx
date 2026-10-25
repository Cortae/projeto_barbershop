import { useState } from 'react'
import { useNavigate } from 'react-router'
import { signIn, setSession } from '@/entities/barber'
import { ROUTES } from '@/shared/config'
import { Banner, Button, TextField } from '@/shared/ui'

export default function CredentialsSignInForm() {
  const [form, setForm] = useState({ email: '', senha: '' })
  const [errors, setErrors] = useState({})
  const [requestError, setRequestError] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const set = (field) => (event) => {
    setForm((current) => ({ ...current, [field]: event.target.value }))
    setErrors((current) => ({ ...current, [field]: undefined }))
    setRequestError(null)
  }

  const submit = async (event) => {
    event.preventDefault()
    const validation = {}
    if (!form.email.trim()) validation.email = 'Informe o e-mail.'
    if (!form.senha) validation.senha = 'Informe a senha.'
    setErrors(validation)
    if (Object.keys(validation).length) return

    setLoading(true)
    setRequestError(null)
    try {
      const user = await signIn(form)
      setSession(user)
      navigate(ROUTES.home, { replace: true })
    } catch (error) {
      setRequestError(error?.message || 'Não foi possível entrar. Tente novamente.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="flex flex-col gap-4" onSubmit={submit} noValidate>
      {requestError && <Banner>{requestError}</Banner>}
      <TextField
        label="E-mail"
        required
        type="email"
        autoComplete="email"
        value={form.email}
        onChange={set('email')}
        error={errors.email}
        disabled={loading}
      />
      <TextField
        label="Senha"
        required
        type="password"
        autoComplete="current-password"
        value={form.senha}
        onChange={set('senha')}
        error={errors.senha}
        disabled={loading}
      />
      <Button type="submit" size="lg" full loading={loading} loadingLabel="Entrando...">
        Entrar
      </Button>
    </form>
  )
}
