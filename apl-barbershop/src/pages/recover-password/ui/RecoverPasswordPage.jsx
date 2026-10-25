import { ArrowLeft } from 'lucide-react'
import { Link, Navigate } from 'react-router'
import { useSession } from '@/entities/barber'
import { RecoverPasswordForm } from '@/features/auth/recover-password'
import { ROUTES } from '@/shared/config'
import { Logo } from '@/widgets/app-layout'

export default function RecoverPasswordPage() {
  const session = useSession()
  if (session) return <Navigate to={ROUTES.home} replace />

  return (
    <main className="flex min-h-dvh flex-col bg-fundo px-4 lg:items-center lg:justify-center">
      <div className="relative flex flex-1 flex-col justify-between py-12 lg:w-full lg:max-w-md lg:flex-none lg:rounded-12 lg:border lg:border-borda lg:bg-superficie lg:p-8">
        <div
          aria-hidden
          className="pointer-events-none absolute inset-x-0 top-0 h-64 opacity-40"
          style={{ background: 'radial-gradient(60% 60% at 50% 0%, color-mix(in srgb, var(--color-primaria) 35%, transparent), transparent)' }}
        />
        <div className="relative flex flex-col gap-12 pt-12 lg:pt-0">
          <Logo large />
          <div>
            <Link
              className="texto-corpo-pequeno mb-6 inline-flex items-center gap-2 font-medium text-primaria hover:underline"
              to={ROUTES.login}
            >
              <ArrowLeft className="size-4" aria-hidden />
              Voltar para o login
            </Link>
            <h1 className="texto-h1">Recuperar senha</h1>
            <p className="mt-3 text-texto-secundario">
              Informe seu e-mail e enviaremos uma nova senha para você.
            </p>
          </div>
        </div>
        <div className="relative mt-12">
          <RecoverPasswordForm />
        </div>
      </div>
    </main>
  )
}
