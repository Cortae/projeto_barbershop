import { Check, Clock, Ban } from 'lucide-react'

const V = {
  neutral: 'bg-superficie-elevada border border-borda-campo text-texto',
  paid: 'bg-sucesso text-fundo',
  pending: 'border border-alerta text-alerta',
  blocked: 'hachura-info border border-info text-texto',
}
const ICONS = { paid: Check, pending: Clock, blocked: Ban }

/** Selo informativo: sempre texto + ícone, nunca só cor. */
export default function Badge({ variant = 'neutral', children }) {
  const Icon = ICONS[variant]
  return (
    <span className={`texto-label inline-flex h-7 items-center gap-1 whitespace-nowrap rounded-full px-3 ${V[variant]}`}>
      {Icon && <Icon className="size-4" aria-hidden />}
      {children}
    </span>
  )
}
