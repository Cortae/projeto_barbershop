import { Loader2 } from 'lucide-react'

const VARIANTS = {
  primary: 'bg-primaria text-fundo hover:bg-primaria-hover active:bg-primaria-pressionado',
  secondary: 'bg-secundaria text-primaria-hover hover:ring-1 hover:ring-primaria-hover active:bg-superficie-elevada',
  'danger-outline': 'border-2 border-erro text-erro hover:bg-superficie-elevada active:bg-superficie',
  danger: 'bg-erro text-fundo hover:ring-2 hover:ring-texto',
  link: 'text-primaria font-medium hover:underline px-0',
  icon: 'border border-borda-campo text-texto hover:bg-superficie-elevada',
}
const SIZES = { lg: 'h-14 px-6', md: 'h-12 px-5', sm: 'h-11 px-4', icon: 'h-11 w-11 shrink-0' }

export default function Button({ variant = 'primary', size = 'md', loading = false, loadingLabel, icon: Icon, full, className = '', children, disabled, ...props }) {
  const isDisabled = disabled || loading
  const state = isDisabled
    ? 'bg-superficie-elevada text-texto-secundario border-transparent hover:ring-0 cursor-not-allowed'
    : VARIANTS[variant]
  return (
    <button
      type="button"
      disabled={isDisabled}
      aria-busy={loading || undefined}
      className={`inline-flex min-h-11 items-center justify-center gap-2 rounded-8 text-base font-semibold leading-none transition-colors ${SIZES[size]} ${full ? 'w-full' : ''} ${state} ${className}`}
      {...props}
    >
      {loading ? <Loader2 className="size-5 animate-spin" aria-hidden /> : Icon && <Icon className="size-5" aria-hidden />}
      {loading && loadingLabel ? loadingLabel : children}
    </button>
  )
}
