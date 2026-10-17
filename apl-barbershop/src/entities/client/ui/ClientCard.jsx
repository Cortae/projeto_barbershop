import { Card } from '@/shared/ui'
import { formatDate } from '@/shared/lib/format'
import { PaymentBadge } from '@/entities/payment'

export default function ClientCard({ client, onClick }) {
  return (
    <Card onClick={onClick} chevron>
      <h3 className="texto-h3">{client.nome}</h3>
      <p className="texto-corpo-pequeno mt-1 text-texto-secundario">{client.telefone}</p>
      <div className="mt-2 flex flex-wrap items-center gap-2">
        <span className="texto-corpo-pequeno text-texto-secundario">
          Último corte: {client.ultimaVisitaEm ? formatDate(client.ultimaVisitaEm) : '—'}
        </span>
        {client.valorPendente > 0 && <PaymentBadge status="pendente" />}
      </div>
    </Card>
  )
}
