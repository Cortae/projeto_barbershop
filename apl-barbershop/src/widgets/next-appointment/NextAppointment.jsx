import { useNavigate } from 'react-router'
import { Card, Button } from '@/shared/ui'
import { formatMoney, formatTime } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

export default function NextAppointment({ appointment }) {
  const navigate = useNavigate()
  if (!appointment) return null
  return (
    <Card highlight className="flex flex-col gap-2">
      <span className="texto-label text-primaria">Próximo corte</span>
      <p className="texto-h2 tabular-nums">
        {formatTime(appointment.horarioInicio)} · {appointment.cliente.nome}
      </p>
      <p className="text-texto-secundario tabular-nums">
        {appointment.servicos.map((servico) => servico.nome).join(', ')} · {formatMoney(appointment.valor)}
      </p>
      <Button variant="secondary" size="sm" className="mt-2 self-start" onClick={() => navigate(ROUTES.appointment(appointment.id))}>
        Ver detalhes
      </Button>
    </Card>
  )
}
