import { useNavigate, useParams } from 'react-router'
import { AppLayout } from '@/widgets/app-layout'
import { CompleteAppointmentButton } from '@/features/appointment/complete-appointment'
import { CancelAppointmentButton } from '@/features/appointment/cancel-appointment'
import { RescheduleButton } from '@/features/appointment/reschedule-appointment'
import { MarkAsPaidButton } from '@/features/appointment/mark-as-paid'
import { useAppointment, AppointmentStatusBadge } from '@/entities/appointment'
import { PaymentBadge, methodLabel } from '@/entities/payment'
import { Card, Skeleton, ErrorState } from '@/shared/ui'
import { formatCurrency, formatDayMonth, formatTime } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

export default function AppointmentDetailsPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { data: a, isLoading, isError, refetch } = useAppointment(id)
  const toSchedule = () => navigate(`${ROUTES.schedule}?vista=dia&data=${a.startsAt.slice(0, 10)}`)

  return (
    <AppLayout title="Detalhe do agendamento" back>
      <div className="mx-auto flex max-w-xl flex-col gap-6">
        {isLoading && <Skeleton className="h-44 rounded-12" />}
        {isError && <ErrorState title="Não foi possível carregar o atendimento." onRetry={refetch} />}
        {a && (
          <>
            <Card className="flex flex-col gap-3">
              <div className="flex flex-wrap items-center gap-2">
                <button className="texto-h2 mr-auto text-left hover:underline" onClick={() => navigate(ROUTES.client(a.clientId))}>{a.client.name}</button>
                <AppointmentStatusBadge status={a.status} />
                <PaymentBadge status={a.paymentStatus} />
              </div>
              <p className="text-texto-secundario tabular-nums">
                {a.service.name} · {formatDayMonth(a.startsAt)} às {formatTime(a.startsAt)} · {a.durationMin} min
              </p>
              <p className="texto-kpi">{formatCurrency(a.priceCents)}</p>
              {a.paymentMethod && <p className="texto-corpo-pequeno text-texto-secundario">Forma de pagamento: {methodLabel(a.paymentMethod)}</p>}
              {a.notes && <p className="texto-corpo-pequeno rounded-8 bg-superficie-elevada p-3">{a.notes}</p>}
            </Card>

            {a.status === 'agendado' && (
              <div className="flex flex-col gap-3">
                <CompleteAppointmentButton appointmentId={a.id} onDone={toSchedule} />
                <RescheduleButton appointmentId={a.id} />
                <CancelAppointmentButton appointmentId={a.id} onDone={toSchedule} />
              </div>
            )}
            {a.status === 'concluido' && a.paymentStatus === 'pendente' && <MarkAsPaidButton appointment={a} size="md" full />}
          </>
        )}
      </div>
    </AppLayout>
  )
}
