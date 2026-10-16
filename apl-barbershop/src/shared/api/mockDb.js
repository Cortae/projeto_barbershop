// Banco em memória que simula o back-end Spring Boot enquanto a API não existe.
import { addDays, today } from '@/shared/lib/date'

const t = today()
const at = (offset, time) => `${addDays(t, offset)}T${time}`

let seq = 100
export const nextId = (prefix) => `${prefix}${++seq}`

export const db = {
  servicos: [
    { id: 's1', nome: 'Corte masculino', valorCentavos: 4500, duracaoMinutos: 30 },
    { id: 's2', nome: 'Barba', valorCentavos: 3000, duracaoMinutos: 30 },
    { id: 's3', nome: 'Corte + barba', valorCentavos: 7000, duracaoMinutos: 60 },
    { id: 's4', nome: 'Sobrancelha', valorCentavos: 1500, duracaoMinutos: 15 },
  ],
  clientes: [
    { id: 'c1', nome: 'Carlos Souza', telefone: '(11) 98765-4321', email: 'carlos@email.com', dataNascimento: '1990-04-12', observacoes: '' },
    { id: 'c2', nome: 'Rafael Lima', telefone: '(11) 91234-5678', email: '', dataNascimento: '', observacoes: 'Prefere máquina 2 nas laterais.' },
    { id: 'c3', nome: 'Bruno Alves', telefone: '(11) 99876-1234', email: '', dataNascimento: '', observacoes: '' },
    { id: 'c4', nome: 'Diego Martins', telefone: '(11) 97654-3210', email: 'diego@email.com', dataNascimento: '', observacoes: '' },
    { id: 'c5', nome: 'Lucas Ferreira', telefone: '(11) 96543-2109', email: '', dataNascimento: '', observacoes: '' },
  ],
  agendamentos: [
    { id: 'a1', clienteId: 'c1', servicoId: 's1', horarioInicio: at(0, '09:00'), status: 'concluido', statusPagamento: 'pago', formaPagamento: 'pix' },
    { id: 'a2', clienteId: 'c2', servicoId: 's3', horarioInicio: at(0, '10:00'), status: 'concluido', statusPagamento: 'pendente' },
    { id: 'a3', clienteId: 'c3', servicoId: 's1', horarioInicio: at(0, '14:00'), status: 'agendado' },
    { id: 'a4', clienteId: 'c4', servicoId: 's2', horarioInicio: at(0, '15:00'), status: 'agendado' },
    { id: 'a5', clienteId: 'c5', servicoId: 's3', horarioInicio: at(0, '16:30'), status: 'agendado' },
    { id: 'a6', clienteId: 'c1', servicoId: 's3', horarioInicio: at(-1, '11:00'), status: 'concluido', statusPagamento: 'pendente' },
    { id: 'a7', clienteId: 'c3', servicoId: 's2', horarioInicio: at(-2, '09:30'), status: 'concluido', statusPagamento: 'pago', formaPagamento: 'dinheiro' },
    { id: 'a8', clienteId: 'c4', servicoId: 's1', horarioInicio: at(-6, '10:00'), status: 'concluido', statusPagamento: 'pago', formaPagamento: 'credito' },
    { id: 'a9', clienteId: 'c2', servicoId: 's1', horarioInicio: at(-9, '17:00'), status: 'concluido', statusPagamento: 'pago', formaPagamento: 'pix' },
    { id: 'a10', clienteId: 'c5', servicoId: 's4', horarioInicio: at(-13, '13:30'), status: 'concluido', statusPagamento: 'pago', formaPagamento: 'debito' },
    { id: 'a11', clienteId: 'c2', servicoId: 's1', horarioInicio: at(1, '09:00'), status: 'agendado' },
    { id: 'a12', clienteId: 'c4', servicoId: 's3', horarioInicio: at(2, '14:00'), status: 'agendado' },
  ].map((a) => {
    const s = { s1: [4500, 30], s2: [3000, 30], s3: [7000, 60], s4: [1500, 15] }[a.servicoId]
    return { observacoes: '', ...a, valorCentavos: s[0], duracaoMinutos: s[1] }
  }),
  bloqueios: [{ id: 'b1', data: addDays(t, 1), inicio: '12:00', fim: '13:00', motivo: 'Almoço' }],
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

export class ApiError extends Error {
  constructor(code, message) {
    super(message)
    this.code = code
  }
}
