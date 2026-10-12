import { PeriodSelector } from '@/shared/ui'
import { addMonths, monthLabel } from '@/shared/lib/date'
import { useMonthParam } from './useMonthParam'

export default function MonthSelector({ disabled }) {
  const [month, setMonth] = useMonthParam()
  return (
    <PeriodSelector
      label={monthLabel(month)}
      prevLabel="Mês anterior"
      nextLabel="Próximo mês"
      disabled={disabled}
      onPrev={() => setMonth(addMonths(month, -1))}
      onNext={() => setMonth(addMonths(month, 1))}
    />
  )
}
