// Banco em memória que simula o back-end Spring Boot enquanto a API não existe.
import { addDays, today } from '@/shared/lib/date'

const t = today()
const at = (offset, time) => `${addDays(t, offset)}T${time}`

let seq = 100
export const nextId = (prefix) => `${prefix}${++seq}`

export const db = {
  services: [
    { id: 's1', name: 'Corte masculino', priceCents: 4500, durationMin: 30 },
    { id: 's2', name: 'Barba', priceCents: 3000, durationMin: 30 },
    { id: 's3', name: 'Corte + barba', priceCents: 7000, durationMin: 60 },
    { id: 's4', name: 'Sobrancelha', priceCents: 1500, durationMin: 15 },
  ],
  clients: [
    { id: 'c1', name: 'Carlos Souza', phone: '(11) 98765-4321', email: 'carlos@email.com', birthDate: '1990-04-12', notes: '' },
    { id: 'c2', name: 'Rafael Lima', phone: '(11) 91234-5678', email: '', birthDate: '', notes: 'Prefere máquina 2 nas laterais.' },
    { id: 'c3', name: 'Bruno Alves', phone: '(11) 99876-1234', email: '', birthDate: '', notes: '' },
    { id: 'c4', name: 'Diego Martins', phone: '(11) 97654-3210', email: 'diego@email.com', birthDate: '', notes: '' },
    { id: 'c5', name: 'Lucas Ferreira', phone: '(11) 96543-2109', email: '', birthDate: '', notes: '' },
  ],
  appointments: [
    { id: 'a1', clientId: 'c1', serviceId: 's1', startsAt: at(0, '09:00'), status: 'concluido', paymentStatus: 'pago', paymentMethod: 'pix' },
    { id: 'a2', clientId: 'c2', serviceId: 's3', startsAt: at(0, '10:00'), status: 'concluido', paymentStatus: 'pendente' },
    { id: 'a3', clientId: 'c3', serviceId: 's1', startsAt: at(0, '14:00'), status: 'agendado' },
    { id: 'a4', clientId: 'c4', serviceId: 's2', startsAt: at(0, '15:00'), status: 'agendado' },
    { id: 'a5', clientId: 'c5', serviceId: 's3', startsAt: at(0, '16:30'), status: 'agendado' },
    { id: 'a6', clientId: 'c1', serviceId: 's3', startsAt: at(-1, '11:00'), status: 'concluido', paymentStatus: 'pendente' },
    { id: 'a7', clientId: 'c3', serviceId: 's2', startsAt: at(-2, '09:30'), status: 'concluido', paymentStatus: 'pago', paymentMethod: 'dinheiro' },
    { id: 'a8', clientId: 'c4', serviceId: 's1', startsAt: at(-6, '10:00'), status: 'concluido', paymentStatus: 'pago', paymentMethod: 'credito' },
    { id: 'a9', clientId: 'c2', serviceId: 's1', startsAt: at(-9, '17:00'), status: 'concluido', paymentStatus: 'pago', paymentMethod: 'pix' },
    { id: 'a10', clientId: 'c5', serviceId: 's4', startsAt: at(-13, '13:30'), status: 'concluido', paymentStatus: 'pago', paymentMethod: 'debito' },
    { id: 'a11', clientId: 'c2', serviceId: 's1', startsAt: at(1, '09:00'), status: 'agendado' },
    { id: 'a12', clientId: 'c4', serviceId: 's3', startsAt: at(2, '14:00'), status: 'agendado' },
  ].map((a) => {
    const s = { s1: [4500, 30], s2: [3000, 30], s3: [7000, 60], s4: [1500, 15] }[a.serviceId]
    return { notes: '', ...a, priceCents: s[0], durationMin: s[1] }
  }),
  blocks: [{ id: 'b1', date: addDays(t, 1), start: '12:00', end: '13:00', reason: 'Almoço' }],
  workingHours: { days: [1, 2, 3, 4, 5, 6], opensAt: '09:00', closesAt: '18:00', breakStart: '12:00', breakEnd: '13:00' },
  expenses: [
    { id: 'e1', description: 'Aluguel', amountCents: 120000, date: `${t.slice(0, 7)}-05` },
    { id: 'e2', description: 'Produtos (pomada, lâminas)', amountCents: 18000, date: `${t.slice(0, 7)}-08` },
    { id: 'e3', description: 'Energia', amountCents: 21000, date: `${t.slice(0, 7)}-10` },
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
