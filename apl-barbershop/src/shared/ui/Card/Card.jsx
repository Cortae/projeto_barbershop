import { ChevronRight } from 'lucide-react'

/** Card padrão; se receber onClick vira botão com hover/foco/ativo. */
export default function Card({ highlight, elevated, onClick, chevron, className = '', children, ...props }) {
  const look = `rounded-12 p-4 ${elevated ? 'bg-superficie-elevada' : 'bg-superficie'} ${highlight ? 'border-2 border-primaria' : 'border border-borda'}`
  if (!onClick) return <div className={`${look} ${className}`} {...props}>{children}</div>
  return (
    <button
      type="button"
      onClick={onClick}
      className={`${look} flex w-full min-h-11 items-center gap-3 text-left transition-colors hover:bg-superficie-elevada active:border-primaria ${className}`}
      {...props}
    >
      <div className="min-w-0 flex-1">{children}</div>
      {chevron && <ChevronRight className="size-5 shrink-0 text-texto-secundario" aria-hidden />}
    </button>
  )
}
