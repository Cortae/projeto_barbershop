export const PAYMENT_METHODS = [
  { value: 'dinheiro', label: 'Dinheiro' },
  { value: 'pix', label: 'Pix' },
  { value: 'debito', label: 'Cartão de débito' },
  { value: 'credito', label: 'Cartão de crédito' },
]
export const methodLabel = (v) => PAYMENT_METHODS.find((m) => m.value === v)?.label ?? ''
