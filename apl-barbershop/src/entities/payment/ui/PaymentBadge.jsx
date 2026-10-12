import { Badge } from '@/shared/ui'
import { formatCurrency } from '@/shared/lib/format'

export default function PaymentBadge({ status, amountCents }) {
  if (status === 'pago') return <Badge variant="paid">Pago</Badge>
  if (status === 'pendente')
    return <Badge variant="pending">{amountCents != null ? `Pendente: ${formatCurrency(amountCents)}` : 'Pendente'}</Badge>
  return null
}
