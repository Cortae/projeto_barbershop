import { timeToMinutes, minutesToTime, fromISODate } from '@/shared/lib/date'

const STEP = 30

/**
 * Monta a grade do dia: { time, state: 'livre'|'ocupado'|'bloqueado', span, appointment?, reason? }.
 * Retorna [] quando o dia está fora do expediente.
 */
export function buildDaySlots(date, hours, appointments, blocks) {
  if (!hours.days.includes(fromISODate(date).getDay())) return []
  const open = timeToMinutes(hours.opensAt)
  const close = timeToMinutes(hours.closesAt)
  const dayAppts = appointments.filter((a) => a.startsAt.slice(0, 10) === date)
  const dayBlocks = blocks.filter((b) => b.date === date)
  if (hours.breakStart && hours.breakEnd) dayBlocks.push({ start: hours.breakStart, end: hours.breakEnd, reason: 'Intervalo' })

  const slots = []
  for (let m = open; m < close; ) {
    const appt = dayAppts.find((a) => {
      const s = timeToMinutes(a.startsAt.slice(11, 16))
      return s >= m && s < m + STEP
    })
    if (appt) {
      const span = Math.max(1, Math.ceil(appt.durationMin / STEP))
      slots.push({ time: minutesToTime(m), state: 'ocupado', span, appointment: appt })
      m += span * STEP
      continue
    }
    const block = dayBlocks.find((b) => m >= timeToMinutes(b.start) && m < timeToMinutes(b.end))
    if (block) {
      const end = timeToMinutes(block.end)
      const span = Math.ceil((end - m) / STEP)
      slots.push({ time: minutesToTime(m), state: 'bloqueado', span, reason: block.reason })
      m = end
      continue
    }
    slots.push({ time: minutesToTime(m), state: 'livre', span: 1 })
    m += STEP
  }
  return slots
}

/** Horários de início livres para um serviço com a duração informada. */
export function freeStartTimes(date, hours, durationMin, isFree) {
  const open = timeToMinutes(hours.opensAt)
  const close = timeToMinutes(hours.closesAt)
  const out = []
  for (let m = open; m + durationMin <= close; m += STEP) {
    const time = minutesToTime(m)
    out.push({ time, free: isFree(`${date}T${time}`) })
  }
  return out
}
