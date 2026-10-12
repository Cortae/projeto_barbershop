import { History } from 'lucide-react'
import { EmptyState, SkeletonList, ErrorState } from '@/shared/ui'
import { formatCurrency, formatDate } from '@/shared/lib/format'
import { useClientAppointments } from '@/entities/appointment'
import { PaymentBadge } from '@/entities/payment'

export default function ClientHistory({ clientId }) {
  const { data, isLoading, isError, refetch } = useClientAppointments(clientId)
  return (
    <section aria-labelledby="historico" className="flex flex-col gap-3">
      <h2 id="historico" className="texto-h2">Histórico de atendimentos</h2>
      {isLoading && <SkeletonList rows={3} height="h-14" />}
      {isError && <ErrorState onRetry={refetch} />}
      {data?.length === 0 && <EmptyState icon={History} title="Nenhum atendimento ainda" text="Os cortes concluídos aparecem aqui." />}
      {data?.length > 0 && (
        <ul className="divide-y divide-borda overflow-hidden rounded-12 border border-borda bg-superficie">
          {data.map((a) => (
            <li key={a.id} className="flex flex-wrap items-center gap-x-3 gap-y-1 p-4">
              <span className="texto-corpo-pequeno tabular-nums text-texto-secundario">{formatDate(a.startsAt)}</span>
              <span className="flex-1">{a.service.name}</span>
              <span className="texto-valor">{formatCurrency(a.priceCents)}</span>
              <PaymentBadge status={a.paymentStatus} />
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
