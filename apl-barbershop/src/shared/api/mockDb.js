// Dados ainda locais: expediente (API adiada) e despesas (fora do fluxo de agendamentos).
import { today } from '@/shared/lib/date'

const t = today()
let seq = 100
export const nextId = (prefix) => `${prefix}${++seq}`

export const db = {
  expediente: { dias: [1, 2, 3, 4, 5, 6], horarioAbertura: '09:00', horarioFechamento: '18:00', inicioIntervalo: '12:00', fimIntervalo: '13:00' },
  despesas: [
    { id: 'e1', descricao: 'Aluguel', valorCentavos: 120000, data: `${t.slice(0, 7)}-05` },
    { id: 'e2', descricao: 'Produtos (pomada, lâminas)', valorCentavos: 18000, data: `${t.slice(0, 7)}-08` },
    { id: 'e3', descricao: 'Energia', valorCentavos: 21000, data: `${t.slice(0, 7)}-10` },
  ],
}

/** Simula latência de rede. */
export const request = (fn, ms = 350) =>
  new Promise((resolve, reject) =>
    setTimeout(() => {
      try {
        resolve(structuredClone(fn()))
      } catch (e) {
        reject(e)
      }
    }, ms),
  )
