import { useNavigate, useParams, useSearchParams } from 'react-router'
import { AppLayout } from '@/widgets/app-layout'
import { AppointmentForm } from '@/features/appointment/create-appointment'
import { useAppointment } from '@/entities/appointment'
import { SkeletonList, ErrorState } from '@/shared/ui'
import { ROUTES } from '@/shared/config'

export default function AppointmentFormPage() {
  const { id } = useParams()
  const [params] = useSearchParams()
  const navigate = useNavigate()
  const existing = useAppointment(id)
  const editing = !!id

  const initial = editing
    ? existing.data
    : { date: params.get('data') ?? '', time: params.get('hora') ?? '', clientId: params.get('clienteId') ?? '' }

  return (
    <AppLayout title={editing ? 'Editar agendamento' : 'Novo agendamento'} back>
      <div className="mx-auto max-w-xl">
        {editing && existing.isLoading && <SkeletonList rows={5} height="h-12" />}
        {editing && existing.isError && <ErrorState title="Não foi possível carregar o atendimento." onRetry={existing.refetch} />}
        {initial && (
          <AppointmentForm
            key={initial.id ?? 'novo'}
            initial={initial}
            onCancel={() => navigate(-1)}
            onSaved={(a) => navigate(`${ROUTES.schedule}?vista=dia&data=${a.startsAt.slice(0, 10)}`)}
          />
        )}
      </div>
    </AppLayout>
  )
}
