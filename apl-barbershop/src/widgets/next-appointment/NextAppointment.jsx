import { useNavigate } from 'react-router'
import { Card, Button } from '@/shared/ui'
import { formatCurrency, formatTime } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

export default function NextAppointment({ appointment }) {
  const navigate = useNavigate()
  if (!appointment) return null
  return (
    <Card highlight className="flex flex-col gap-2">
      <span className="texto-label text-primaria">Próximo corte</span>
      <p className="texto-h2 tabular-nums">
        {formatTime(appointment.startsAt)} · {appointment.client.name}
      </p>
      <p className="text-texto-secundario tabular-nums">
        {appointment.service.name} · {formatCurrency(appointment.priceCents)}
      </p>
      <Button variant="secondary" size="sm" className="mt-2 self-start" onClick={() => navigate(ROUTES.appointment(appointment.id))}>
        Ver detalhes
      </Button>
    </Card>
  )
}
