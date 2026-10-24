import { useNavigate } from 'react-router'
import { PartyPopper } from 'lucide-react'
import { EmptyState } from '@/shared/ui'
import { formatCurrency, formatDayMonth } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'
import { PaymentBadge } from '@/entities/payment'
import { MarkAsPaidButton } from '@/features/appointment/mark-as-paid'

export default function PendingPaymentsList({ items }) {
  const navigate = useNavigate()
  const total = items.reduce((s, a) => s + a.valorCentavos, 0)
  return (
    <section aria-labelledby="pendentes" className="flex flex-col gap-3">
      <div>
        <h2 id="pendentes" className="texto-h2">Pagamentos pendentes</h2>
        {items.length > 0 && (
          <p className="texto-corpo-pequeno mt-1 text-texto-secundario">
            Falta receber {formatCurrency(total)} de {items.length} {items.length === 1 ? 'cliente' : 'clientes'}.
          </p>
        )}
      </div>
      {items.length === 0 ? (
        <EmptyState icon={PartyPopper} title="Tudo em dia" text="Nenhum pagamento pendente neste mês." />
      ) : (
        <ul className="grid gap-3 lg:grid-cols-2">
          {items.map((a) => (
            <li key={a.id}>
              <div
                role="link"
                tabIndex={0}
                onClick={() => navigate(ROUTES.appointment(a.id))}
                onKeyDown={(e) => e.key === 'Enter' && navigate(ROUTES.appointment(a.id))}
                className="flex cursor-pointer flex-col gap-2 rounded-12 border border-borda bg-superficie p-4 hover:bg-superficie-elevada"
              >
                <div className="flex items-start justify-between gap-2">
                  <span className="font-medium">{a.cliente.nome} · {a.servico.nome}</span>
                  <PaymentBadge status="pendente" />
                </div>
                <div className="flex items-center justify-between gap-2">
                  <span className="texto-corpo-pequeno tabular-nums text-texto-secundario">
                    {formatDayMonth(a.horarioInicio)} · <span className="texto-valor text-alerta">{formatCurrency(a.valorCentavos)}</span>
                  </span>
                  <MarkAsPaidButton appointment={a} />
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
