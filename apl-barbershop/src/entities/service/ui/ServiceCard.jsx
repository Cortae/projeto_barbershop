import { Card } from '@/shared/ui'
import { formatCurrency } from '@/shared/lib/format'

export default function ServiceCard({ service, action }) {
  return (
    <Card className="flex items-center gap-3">
      <div className="min-w-0 flex-1">
        <h3 className="texto-h3">{service.nome}</h3>
        <p className="texto-corpo-pequeno mt-1 text-texto-secundario tabular-nums">
          {service.duracaoMinutos} min · {formatCurrency(service.valorCentavos)}
        </p>
      </div>
      {action}
    </Card>
  )
}
