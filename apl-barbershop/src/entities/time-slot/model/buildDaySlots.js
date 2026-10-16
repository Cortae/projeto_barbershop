import { timeToMinutes, minutesToTime, fromISODate } from '@/shared/lib/date'

const STEP = 30

/**
 * Monta a grade do dia: { horario, estado: 'livre'|'ocupado'|'bloqueado', intervalos, agendamento?, motivo? }.
 * Retorna [] quando o dia está fora do expediente.
 */
export function buildDaySlots(date, hours, appointments, blocks) {
  if (!hours.dias.includes(fromISODate(date).getDay())) return []
  const open = timeToMinutes(hours.horarioAbertura)
  const close = timeToMinutes(hours.horarioFechamento)
  const dayAppts = appointments.filter((a) => a.horarioInicio.slice(0, 10) === date)
  const dayBlocks = blocks.filter((b) => b.data === date)
  if (hours.inicioIntervalo && hours.fimIntervalo) dayBlocks.push({ inicio: hours.inicioIntervalo, fim: hours.fimIntervalo, motivo: 'Intervalo' })

  const slots = []
  for (let m = open; m < close; ) {
    const appt = dayAppts.find((a) => {
      const s = timeToMinutes(a.horarioInicio.slice(11, 16))
      return s >= m && s < m + STEP
    })
    if (appt) {
      const span = Math.max(1, Math.ceil(appt.duracaoMinutos / STEP))
      slots.push({ horario: minutesToTime(m), estado: 'ocupado', intervalos: span, agendamento: appt })
      m += span * STEP
      continue
    }
    const block = dayBlocks.find((b) => m >= timeToMinutes(b.inicio) && m < timeToMinutes(b.fim))
    if (block) {
      const end = timeToMinutes(block.fim)
      const span = Math.ceil((end - m) / STEP)
      slots.push({ horario: minutesToTime(m), estado: 'bloqueado', intervalos: span, motivo: block.motivo })
      m = end
      continue
    }
    slots.push({ horario: minutesToTime(m), estado: 'livre', intervalos: 1 })
    m += STEP
  }
  return slots
}

/** Horários de início livres para um serviço com a duração informada. */
export function freeStartTimes(date, hours, durationMin, isFree) {
  const open = timeToMinutes(hours.horarioAbertura)
  const close = timeToMinutes(hours.horarioFechamento)
  const out = []
  for (let m = open; m + durationMin <= close; m += STEP) {
    const time = minutesToTime(m)
    const intervalStart = hours.inicioIntervalo ? timeToMinutes(hours.inicioIntervalo) : null
    const intervalEnd = hours.fimIntervalo ? timeToMinutes(hours.fimIntervalo) : null
    const overlapsInterval = intervalStart != null && intervalEnd != null && m < intervalEnd && intervalStart < m + durationMin
    out.push({ horario: time, livre: !overlapsInterval && isFree(`${date}T${time}`) })
  }
  return out
}
