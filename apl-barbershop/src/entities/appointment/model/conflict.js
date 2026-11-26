import { timeToMinutes } from '@/shared/lib/date'

const range = (horarioInicio, duracao) => {
  const s = timeToMinutes(horarioInicio.slice(11, 16))
  return [s, s + duracao]
}
const overlaps = ([a1, a2], [b1, b2]) => a1 < b2 && b1 < a2

/** Verifica se [horarioInicio, +duracao) colide com atendimentos ou bloqueios do dia. */
export function findConflict({ horarioInicio, duracaoMinutos, ignorarId }, appointments, blocks) {
  const date = horarioInicio.slice(0, 10)
  const r = range(horarioInicio, duracaoMinutos)
  const appt = appointments.find(
    (a) => a.id !== ignorarId && a.status !== 'cancelado' && a.horarioInicio.slice(0, 10) === date && overlaps(r, range(a.horarioInicio, a.duracaoMinutos)),
  )
  if (appt) return 'appointment'
  const block = blocks.find((b) => b.data === date && overlaps(r, [timeToMinutes(b.inicio), timeToMinutes(b.fim)]))
  return block ? 'block' : null
}
