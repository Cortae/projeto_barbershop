import { Link, useNavigate } from 'react-router'
import { CalendarCheck } from 'lucide-react'
import { Button, EmptyState } from '@/shared/ui'
import { ROUTES } from '@/shared/config'
import { AppointmentCard } from '@/entities/appointment'

export default function TodayAgenda({ appointments }) {
  const navigate = useNavigate()
  return (
    <section aria-labelledby="agenda-hoje" className="flex flex-col gap-3">
      <div className="flex items-center justify-between">
        <h2 id="agenda-hoje" className="texto-h2">Agenda de hoje</h2>
        <Link to={ROUTES.schedule} className="inline-flex min-h-11 items-center font-medium text-primaria hover:underline">Ver agenda completa</Link>
      </div>
      {appointments.length === 0 ? (
        <EmptyState
          icon={CalendarCheck}
          title="Agenda livre hoje"
          text={'Toque em "Novo agendamento" para marcar o primeiro corte.'}
          action={(
            <div className="hidden lg:block">
              <Button onClick={() => navigate(ROUTES.appointmentNew)}>Novo agendamento</Button>
            </div>
          )}
        />
      ) : (
        <ul className="flex flex-col gap-3">
          {appointments.map((a) => (
            <li key={a.id}><AppointmentCard appointment={a} onClick={() => navigate(ROUTES.appointment(a.id))} /></li>
          ))}
        </ul>
      )}
    </section>
  )
}
