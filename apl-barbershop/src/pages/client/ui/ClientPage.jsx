import { useNavigate, useParams, useSearchParams } from 'react-router'
import { CalendarPlus } from 'lucide-react'
import { AppLayout } from '@/widgets/app-layout'
import { ClientHistory } from '@/widgets/client-history'
import { ClientForm } from '@/features/client/upsert-client'
import { useClient } from '@/entities/client'
import { Button, SkeletonList, ErrorState } from '@/shared/ui'
import { ROUTES } from '@/shared/config'

export default function ClientPage() {
  const { id } = useParams()
  const [params] = useSearchParams()
  const navigate = useNavigate()
  const fromAppointment = params.get('retorno') === 'agendamento'
  const { data, isLoading, isError, refetch } = useClient(id)

  const onSaved = (c) =>
    fromAppointment ? navigate(`${ROUTES.appointmentNew}?clienteId=${c.id}`, { replace: true }) : navigate(ROUTES.clients)

  return (
    <AppLayout title={id ? data?.name ?? 'Cliente' : 'Novo cliente'} back>
      <div className="grid gap-8 lg:grid-cols-[minmax(0,1fr)_minmax(0,1fr)]">
        <section aria-labelledby="dados-cliente" className="flex flex-col gap-4">
          <h2 id="dados-cliente" className="texto-h2 hidden lg:block">Dados do cliente</h2>
          {id && isLoading && <SkeletonList rows={5} height="h-12" />}
          {isError && <ErrorState title="Não foi possível carregar o cliente" onRetry={refetch} />}
          {(!id || data) && <ClientForm key={data?.id ?? 'novo'} client={data} onSaved={onSaved} />}
        </section>
        {id && data && (
          <div className="flex flex-col gap-4">
            <ClientHistory clientId={id} />
            <Button variant="secondary" icon={CalendarPlus} onClick={() => navigate(`${ROUTES.appointmentNew}?clienteId=${id}`)}>Agendar corte</Button>
          </div>
        )}
      </div>
    </AppLayout>
  )
}
