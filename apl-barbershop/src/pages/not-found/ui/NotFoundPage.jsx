import { useNavigate } from 'react-router'
import { Compass } from 'lucide-react'
import { EmptyState, Button } from '@/shared/ui'
import { ROUTES } from '@/shared/config'

export default function NotFoundPage() {
  const navigate = useNavigate()
  return (
    <main className="flex min-h-dvh items-center justify-center">
      <EmptyState icon={Compass} title="Página não encontrada" text="Esse endereço não existe. Volte para o início." action={<Button onClick={() => navigate(ROUTES.home)}>Ir para o início</Button>} />
    </main>
  )
}
