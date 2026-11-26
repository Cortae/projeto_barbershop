import { useNavigate } from 'react-router'
import { Card, Button } from '@/shared/ui'
import { formatMoney, formatTime } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'
import { formatFinancialValue } from '@/features/finance/toggle-financial-values'

export default function NextAppointment({ appointment, valuesVisible = true }) {
  const navigate = useNavigate()
  if (!appointment) return null
  return (
    <Card highlight className="flex flex-col gap-2">
      <span className="texto-label text-primaria">Próximo corte</span>
      <p className="texto-h2 tabular-nums">
        {formatTime(appointment.horarioInicio)} · {appointment.cliente.nome}
      </p>
      <p className="text-texto-secundario tabular-nums">
        {appointment.servicos.map((servico) => servico.nome).join(', ')} · {formatFinancialValue(formatMoney(appointment.valor), valuesVisible)}
      </p>
      <Button variant="secondary" size="sm" className="mt-2 self-start" onClick={() => navigate(ROUTES.appointment(appointment.id))}>
        Ver detalhes
      </Button>
    </Card>
  )
}
