export const PAYMENT_METHODS = [
  { value: 'dinheiro', label: 'Dinheiro' },
  { value: 'pix', label: 'Pix' },
  { value: 'debito', label: 'Cartão de débito' },
  { value: 'credito', label: 'Cartão de crédito' },
]
export const methodLabel = (v) => {
  if (!v) return ''
  const normalized = v.toLowerCase()
  return PAYMENT_METHODS.find((m) => m.value === normalized)?.label
    ?? v.replaceAll('_', ' ').toLowerCase().replace(/^./, (letter) => letter.toUpperCase())
}
