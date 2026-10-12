import { Card } from '@/shared/ui'
import { formatTime } from '@/shared/lib/format'
import AppointmentStatusBadge from './AppointmentStatusBadge'

/** Item de agenda: horário, "Cliente · Serviço" e selo. */
export default function AppointmentCard({ appointment, onClick }) {
  return (
    <Card onClick={onClick} chevron>
      <div className="flex items-center gap-3">
        <span className="texto-label w-12 shrink-0 tabular-nums text-texto-secundario">{formatTime(appointment.startsAt)}</span>
        <span className="min-w-0 flex-1 truncate">
          {appointment.client?.name} · {appointment.service?.name}
        </span>
        <AppointmentStatusBadge status={appointment.status} />
      </div>
    </Card>
  )
}
