// Padrões brasileiros (RNF08). Valores monetários em centavos.
const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export const formatCurrency = (cents = 0) => currency.format(cents / 100).replace(/\u00a0/g, ' ')
export const formatMoney = (value = 0) => currency.format(Number(value)).replace(/\u00a0/g, ' ')

/** "35,00" | "35" | "1.234,50" → centavos */
export function parseCurrency(text) {
  if (text === '' || text == null) return NaN
  const normalized = String(text).replace(/\./g, '').replace(',', '.').replace(/[^\d.]/g, '')
  return Math.round(parseFloat(normalized) * 100)
}

export const centsToInput = (cents) => (cents / 100).toFixed(2).replace('.', ',')
export const moneyToInput = (value) => Number(value).toFixed(2).replace('.', ',')
export const parseMoney = (text) => {
  const cents = parseCurrency(text)
  return Number.isNaN(cents) ? NaN : cents / 100
}

/** "2026-10-06" → "06/10/2026" */
export const formatDate = (iso) => (iso ? iso.slice(0, 10).split('-').reverse().join('/') : '')
/** "2026-10-06" → "06/10" */
export const formatDayMonth = (iso) => formatDate(iso).slice(0, 5)
/** "2026-10-06T14:30" → "14:30" */
export const formatTime = (iso) => (iso ? iso.slice(11, 16) : '')
