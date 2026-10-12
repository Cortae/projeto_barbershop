import { timeToMinutes } from '@/shared/lib/date'

const range = (startsAt, dur) => {
  const s = timeToMinutes(startsAt.slice(11, 16))
  return [s, s + dur]
}
const overlaps = ([a1, a2], [b1, b2]) => a1 < b2 && b1 < a2

/** Verifica se [startsAt, +dur) colide com atendimentos ou bloqueios do dia. */
export function findConflict({ startsAt, durationMin, ignoreId }, appointments, blocks) {
  const date = startsAt.slice(0, 10)
  const r = range(startsAt, durationMin)
  const appt = appointments.find(
    (a) => a.id !== ignoreId && a.status !== 'cancelado' && a.startsAt.slice(0, 10) === date && overlaps(r, range(a.startsAt, a.durationMin)),
  )
  if (appt) return 'appointment'
  const block = blocks.find((b) => b.date === date && overlaps(r, [timeToMinutes(b.start), timeToMinutes(b.end)]))
  return block ? 'block' : null
}
