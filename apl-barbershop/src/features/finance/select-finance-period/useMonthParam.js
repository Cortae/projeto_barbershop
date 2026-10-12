import { useSearchParams } from 'react-router'
import { currentMonth } from '@/shared/lib/date'

export function useMonthParam() {
  const [params, setParams] = useSearchParams()
  const month = params.get('periodo') || currentMonth()
  return [month, (nextMonth) => setParams({ periodo: nextMonth }, { replace: true })]
}
