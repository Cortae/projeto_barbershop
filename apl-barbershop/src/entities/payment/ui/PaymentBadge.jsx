import { Badge } from '@/shared/ui'
import { formatCurrency } from '@/shared/lib/format'

export default function PaymentBadge({ status, valorCentavos }) {
  if (status === 'pago') return <Badge variant="paid">Pago</Badge>
  if (status === 'pendente')
    return <Badge variant="pending">{valorCentavos != null ? `Pendente: ${formatCurrency(valorCentavos)}` : 'Pendente'}</Badge>
  return null
}
