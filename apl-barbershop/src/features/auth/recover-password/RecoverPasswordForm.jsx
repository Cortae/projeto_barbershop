import { useState } from 'react'
import { Link } from 'react-router'
import { recoverPassword } from '@/entities/barber'
import { ROUTES } from '@/shared/config'
import { Banner, Button, TextField } from '@/shared/ui'

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export default function RecoverPasswordForm() {
  const [email, setEmail] = useState('')
  const [emailError, setEmailError] = useState(null)
  const [requestError, setRequestError] = useState(null)
  const [loading, setLoading] = useState(false)
  const [sent, setSent] = useState(false)

  const changeEmail = (event) => {
    setEmail(event.target.value)
    setEmailError(null)
    setRequestError(null)
  }

  const submit = async (event) => {
    event.preventDefault()
    const normalizedEmail = email.trim()

    if (!normalizedEmail) {
      setEmailError('Informe o e-mail.')
      return
    }

    if (!EMAIL_PATTERN.test(normalizedEmail)) {
      setEmailError('Informe um e-mail válido.')
      return
    }

    setLoading(true)
    setRequestError(null)

    try {
      await recoverPassword(normalizedEmail)
      setSent(true)
    } catch (error) {
      if (error?.code === '400') {
        setEmailError('Informe um e-mail válido.')
      } else if (error?.code === '404') {
        setEmailError('Não encontramos uma conta com este e-mail.')
      } else if (error?.code === 'network' || error?.code === 'timeout') {
        setRequestError(error.message)
      } else {
        setRequestError('Não foi possível enviar a nova senha. Tente novamente mais tarde.')
      }
    } finally {
      setLoading(false)
    }
  }

  if (sent) {
    return (
      <div className="flex flex-col gap-6">
        <Banner variant="info">
          Enviamos uma nova senha para <strong>{email.trim()}</strong>. Verifique também a caixa de spam.
        </Banner>
        <Link
          className="inline-flex min-h-14 w-full items-center justify-center rounded-8 bg-primaria px-6 font-semibold text-fundo hover:bg-primaria-hover"
          to={ROUTES.login}
        >
          Voltar para o login
        </Link>
      </div>
    )
  }

  return (
    <form className="flex flex-col gap-4" onSubmit={submit} noValidate>
      {requestError && <Banner>{requestError}</Banner>}
      <TextField
        label="E-mail"
        required
        type="email"
        autoComplete="email"
        value={email}
        onChange={changeEmail}
        error={emailError}
        hint="Use o mesmo e-mail cadastrado na sua conta."
        disabled={loading}
      />
      <Button type="submit" size="lg" full loading={loading} loadingLabel="Enviando...">
        Enviar nova senha
      </Button>
    </form>
  )
}
