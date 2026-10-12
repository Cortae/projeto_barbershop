import { AlertCircle, Info, ImageOff } from 'lucide-react'
import Button from '../button/Button'

export function Skeleton({ className = '' }) {
  return <div className={`animate-pulse rounded-8 bg-superficie-elevada ${className}`} aria-hidden />
}

export function SkeletonList({ rows = 3, height = 'h-20' }) {
  return (
    <div aria-busy="true" aria-label="Carregando" className="flex flex-col gap-3">
      {Array.from({ length: rows }, (_, i) => (
        <Skeleton key={i} className={`${height} rounded-12`} />
      ))}
    </div>
  )
}

export function EmptyState({ icon: Icon = ImageOff, title, text, action }) {
  return (
    <div className="flex flex-col items-center gap-6 px-4 py-12 text-center">
      <div className="flex size-20 items-center justify-center rounded-full bg-superficie-elevada" aria-hidden>
        <Icon className="size-9 text-texto-secundario" />
      </div>
      <div>
        <h2 className="texto-h3">{title}</h2>
        <p className="texto-corpo-pequeno mt-2 text-texto-secundario">{text}</p>
      </div>
      {action}
    </div>
  )
}

/** Erro de conexão com "Tentar novamente" (RNF07). */
export function ErrorState({ title = 'Não foi possível carregar os dados', text = 'Verifique sua conexão e tente novamente.', onRetry }) {
  return (
    <div role="alert" className="flex flex-col items-start gap-3 rounded-12 border border-erro bg-superficie p-4">
      <div className="flex items-center gap-2">
        <AlertCircle className="size-6 text-erro" aria-hidden />
        <span className="texto-label">{title}</span>
      </div>
      <p className="texto-corpo-pequeno text-texto-secundario">{text}</p>
      {onRetry && <Button size="sm" onClick={onRetry}>Tentar novamente</Button>}
    </div>
  )
}

export function Banner({ variant = 'error', children }) {
  const color = variant === 'error' ? 'border-erro' : 'border-info'
  const Icon = variant === 'error' ? AlertCircle : Info
  return (
    <div role={variant === 'error' ? 'alert' : 'status'} className={`flex items-start gap-3 rounded-12 border ${color} bg-superficie p-4`}>
      <Icon className={`size-6 shrink-0 ${variant === 'error' ? 'text-erro' : 'text-info'}`} aria-hidden />
      <p className="texto-corpo-pequeno">{children}</p>
    </div>
  )
}
