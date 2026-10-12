import { Badge } from '@/shared/ui'

const LABELS = { agendado: 'Agendado', concluido: 'Concluído', cancelado: 'Cancelado' }
export default function AppointmentStatusBadge({ status }) {
  return <Badge>{LABELS[status]}</Badge>
}
