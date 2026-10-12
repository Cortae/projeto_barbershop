const pad = (n) => String(n).padStart(2, '0')

export const toISODate = (d) => `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
export const fromISODate = (iso) => {
  const [y, m, d] = iso.slice(0, 10).split('-').map(Number)
  return new Date(y, m - 1, d)
}
export const today = () => toISODate(new Date())
export const addDays = (iso, n) => {
  const d = fromISODate(iso)
  d.setDate(d.getDate() + n)
  return toISODate(d)
}
export const startOfWeek = (iso) => addDays(iso, -fromISODate(iso).getDay())
export const weekDays = (iso) => Array.from({ length: 7 }, (_, i) => addDays(startOfWeek(iso), i))

export const WEEKDAYS_SHORT = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb']
const WEEKDAYS_LONG = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado']
export const MONTHS = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']

export const weekdayLong = (iso) => WEEKDAYS_LONG[fromISODate(iso).getDay()]
export const weekdayShort = (iso) => WEEKDAYS_SHORT[fromISODate(iso).getDay()]

/** Mês no formato "YYYY-MM" */
export const currentMonth = () => today().slice(0, 7)
export const addMonths = (month, n) => {
  const [y, m] = month.split('-').map(Number)
  const d = new Date(y, m - 1 + n, 1)
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}`
}
export const monthLabel = (month) => {
  const [y, m] = month.split('-').map(Number)
  return `${MONTHS[m - 1]} ${y}`
}

export const timeToMinutes = (t) => {
  const [h, m] = t.split(':').map(Number)
  return h * 60 + m
}
export const minutesToTime = (min) => `${pad(Math.floor(min / 60))}:${pad(min % 60)}`

export function greeting(date = new Date()) {
  const h = date.getHours()
  if (h < 12) return 'Bom dia'
  if (h < 18) return 'Boa tarde'
  return 'Boa noite'
}
