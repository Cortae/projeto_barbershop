import { useNavigate } from 'react-router'
import { Card } from '@/shared/ui'
import { formatMoney } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'
import { summarizeAppointments } from './model/summary'

export default function DaySummary({ appointments }) {
  const navigate = useNavigate()
  const { received, toReceive } = summarizeAppointments(appointments)
  return (
    <Card onClick={() => navigate(ROUTES.finance)} chevron aria-label={`Hoje: recebido ${formatMoney(received)}, a receber ${formatMoney(toReceive)}. Abrir financeiro`}>
      <h2 className="texto-h3">Hoje</h2>
      <div className="mt-3 grid grid-cols-2 gap-4">
        <div>
          <p className="texto-label text-texto-secundario">Recebido</p>
          <p className="texto-kpi mt-1 text-sucesso">{formatMoney(received)}</p>
        </div>
        <div>
          <p className="texto-label text-texto-secundario">A receber</p>
          <p className="texto-kpi mt-1 text-alerta">{formatMoney(toReceive)}</p>
        </div>
      </div>
    </Card>
  )
}
