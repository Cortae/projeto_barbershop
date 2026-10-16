import { useState } from 'react'
import { useNavigate } from 'react-router'
import { Button, Banner } from '@/shared/ui'
import { setSession } from '@/entities/barber'
import { ROUTES } from '@/shared/config'

const GoogleIcon = () => (
  <svg viewBox="0 0 24 24" className="size-5" aria-hidden>
    <path fill="currentColor" d="M21.35 11.1H12v2.98h5.35c-.23 1.4-1.66 4.1-5.35 4.1-3.22 0-5.85-2.67-5.85-5.96S8.78 6.26 12 6.26c1.83 0 3.06.78 3.76 1.45l2.56-2.47C16.7 3.7 14.56 2.75 12 2.75 6.9 2.75 2.75 6.9 2.75 12S6.9 21.25 12 21.25c5.34 0 8.88-3.75 8.88-9.04 0-.6-.07-1.06-.15-1.51z" />
  </svg>
)

/** Simula o Google Auth; trocar pela integração GIS + back-end. */
export default function GoogleSignInButton() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  const signIn = () => {
    setLoading(true)
    setError(null)
    setTimeout(() => {
      setSession({ id: 2, barbeiroId: 2, nome: 'Lucas Almeida', email: 'lucas@barbershop.test' })
      navigate(ROUTES.home, { replace: true })
    }, 700)
  }

  return (
    <div className="flex flex-col gap-4">
      {error && <Banner>{error}</Banner>}
      <Button size="lg" full icon={GoogleIcon} loading={loading} loadingLabel="Entrando..." onClick={signIn}>
        Entrar com Google
      </Button>
    </div>
  )
}
