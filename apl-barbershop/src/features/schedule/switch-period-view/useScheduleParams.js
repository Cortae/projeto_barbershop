import { useSearchParams } from 'react-router'
import { today } from '@/shared/lib/date'

export function useScheduleParams() {
  const [params, setParams] = useSearchParams()
  const vista = params.get('vista') === 'semana' ? 'semana' : 'dia'
  const data = params.get('data') || today()
  const atualizar = (proximo) => setParams({ vista: proximo.vista ?? vista, data: proximo.data ?? data }, { replace: true })
  return {
    vista,
    data,
    definirVista: (valor) => atualizar({ vista: valor }),
    definirData: (valor) => atualizar({ data: valor }),
    irParaDia: (valor) => atualizar({ vista: 'dia', data: valor }),
  }
}
