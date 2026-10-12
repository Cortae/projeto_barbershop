import { Card } from '@/shared/ui'
import { formatDate } from '@/shared/lib/format'
import { PaymentBadge } from '@/entities/payment'

export default function ClientCard({ client, onClick }) {
  return (
    <Card onClick={onClick} chevron>
      <h3 className="texto-h3">{client.name}</h3>
      <p className="texto-corpo-pequeno mt-1 text-texto-secundario">{client.phone}</p>
      <div className="mt-2 flex flex-wrap items-center gap-2">
        <span className="texto-corpo-pequeno text-texto-secundario">
          Último corte: {client.lastVisitAt ? formatDate(client.lastVisitAt) : '—'}
        </span>
        {client.pendingCents > 0 && <PaymentBadge status="pendente" amountCents={client.pendingCents} />}
      </div>
    </Card>
  )
}
