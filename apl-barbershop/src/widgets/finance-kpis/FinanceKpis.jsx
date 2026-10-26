import { useNavigate } from 'react-router'
import { ChevronRight } from 'lucide-react'
import { formatMoney } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

function Kpi({ label, value, tone = 'text-texto', highlight, highlightTone = 'border-primaria', onClick }) {
  const cls = `flex flex-col gap-2 rounded-12 p-4 text-left ${highlight ? `bg-superficie-elevada border-2 ${highlightTone}` : 'bg-superficie border border-borda'}`
  const content = (
    <>
      <span className="texto-label flex items-center justify-between text-texto-secundario">
        {label} {onClick && <ChevronRight className="size-5" aria-hidden />}
      </span>
      <span className={`texto-kpi ${tone} text-[26px] sm:text-[32px]`}>{formatMoney(value)}</span>
    </>
  )
  return onClick ? (
    <button onClick={onClick} className={`${cls} hover:bg-superficie-elevada`}>{content}</button>
  ) : (
    <div className={cls}>{content}</div>
  )
}

export default function FinanceKpis({ summary }) {
  const navigate = useNavigate()
  return (
    <div className="grid grid-cols-2 gap-3 lg:grid-cols-4 lg:gap-6">
      <Kpi label="Receita bruta" value={summary.valorBruto} />
      <Kpi label="Despesas" value={summary.despesas} onClick={() => navigate(ROUTES.expenses)} />
      <Kpi
        label="Receita líquida"
        value={summary.valorLiquido}
        tone={summary.valorLiquido < 0 ? 'text-erro' : 'text-primaria'}
        highlightTone={summary.valorLiquido < 0 ? 'border-erro' : 'border-primaria'}
        highlight
      />
      <Kpi label="A receber" value={summary.valorAReceber} tone="text-alerta" />
    </div>
  )
}
