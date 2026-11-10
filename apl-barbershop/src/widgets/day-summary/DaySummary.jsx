import { useNavigate } from 'react-router'
import { Card } from '@/shared/ui'
import { formatMoney } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'
import { summarizeAppointments } from './model/summary'
import { formatFinancialValue } from '@/features/finance/toggle-financial-values'

export default function DaySummary({ appointments, valuesVisible = true }) {
  const navigate = useNavigate()
  const { received, toReceive } = summarizeAppointments(appointments)
  const receivedLabel = formatFinancialValue(formatMoney(received), valuesVisible)
  const toReceiveLabel = formatFinancialValue(formatMoney(toReceive), valuesVisible)
  return (
    <Card onClick={() => navigate(ROUTES.finance)} chevron aria-label={`Hoje: recebido ${receivedLabel}, a receber ${toReceiveLabel}. Abrir financeiro`}>
      <h2 className="texto-h3">Hoje</h2>
      <div className="mt-3 grid grid-cols-2 gap-4">
        <div>
          <p className="texto-label text-texto-secundario">Recebido</p>
          <p className="texto-kpi mt-1 text-sucesso">{receivedLabel}</p>
        </div>
        <div>
          <p className="texto-label text-texto-secundario">A receber</p>
          <p className="texto-kpi mt-1 text-alerta">{toReceiveLabel}</p>
        </div>
      </div>
    </Card>
  )
}
