import { useSearchParams } from 'react-router'
import { today } from '@/shared/lib/date'

export function useScheduleParams() {
  const [params, setParams] = useSearchParams()
  const view = params.get('vista') === 'semana' ? 'semana' : 'dia'
  const date = params.get('data') || today()
  const update = (next) => setParams({ vista: next.view ?? view, data: next.date ?? date }, { replace: true })
  return { view, date, setView: (v) => update({ view: v }), setDate: (d) => update({ date: d }), goToDay: (d) => update({ view: 'dia', date: d }) }
}
