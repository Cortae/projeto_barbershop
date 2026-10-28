import { useNavigate } from 'react-router'
import { AppLayout } from '@/widgets/app-layout'
import { NextAppointment } from '@/widgets/next-appointment'
import { DaySummary } from '@/widgets/day-summary'
import { TodayAgenda } from '@/widgets/today-agenda'
import { useAppointments } from '@/entities/appointment'
import { useSession } from '@/entities/barber'
import { Skeleton, SkeletonList, ErrorState } from '@/shared/ui'
import { greeting, today } from '@/shared/lib/date'
import { ROUTES } from '@/shared/config'

export default function HomePage() {
  const navigate = useNavigate()
  const session = useSession()
  const t = today()
  const { data, isLoading, isError, refetch } = useAppointments(t, t)
  const now = new Date().toTimeString().slice(0, 5)
  const activeAppointments = data?.filter((a) => a.status !== 'cancelado') ?? []
  const scheduled = data?.filter((a) => a.status === 'agendado') ?? []
  const next = scheduled.find((a) => a.horarioInicio.slice(11, 16) >= now) ?? scheduled[0]

  return (
    <AppLayout title="Início" variant="home" primaryAction={{ label: 'Novo agendamento', onClick: () => navigate(ROUTES.appointmentNew) }}>
      <div className="flex flex-col gap-6">
        <div>
          <h2 className="texto-h1">{greeting()}, {session?.nome}!</h2>
          {isLoading ? (
            <Skeleton className="mt-2 h-6 w-48" />
          ) : (
            data && <p className="mt-1 text-texto-secundario">Você tem {activeAppointments.length} {activeAppointments.length === 1 ? 'corte' : 'cortes'} hoje.</p>
          )}
        </div>
        {isError && <ErrorState title="Não foi possível carregar a agenda de hoje" onRetry={refetch} />}
        {isLoading && (
          <div className="grid gap-4 lg:grid-cols-2" aria-busy="true">
            <Skeleton className="h-40 rounded-12" />
            <Skeleton className="h-40 rounded-12" />
            <div className="lg:col-span-2"><SkeletonList rows={3} height="h-14" /></div>
          </div>
        )}
        {data && (
          <>
            <div className="grid gap-4 lg:grid-cols-2">
              <NextAppointment appointment={next} />
              <DaySummary appointments={data} />
            </div>
            <TodayAgenda appointments={activeAppointments} />
          </>
        )}
      </div>
    </AppLayout>
  )
}
