import { Check } from 'lucide-react'

export default function Chip({ selected, children, ...props }) {
  return (
    <button
      type="button"
      aria-pressed={selected}
      className={`texto-label inline-flex min-h-11 items-center gap-2 rounded-full px-4 transition-colors ${
        selected ? 'bg-secundaria text-primaria-hover' : 'bg-superficie-elevada border border-borda-campo text-texto hover:border-texto-secundario'
      }`}
      {...props}
    >
      {selected && <Check className="size-5" aria-hidden />}
      {children}
    </button>
  )
}

/** Opção única segmentada (Pago / Pendente). */
export function Segmented({ label, options, value, onChange }) {
  return (
    <fieldset className="flex flex-col gap-2">
      <legend className="texto-label mb-2">{label}</legend>
      <div className="grid grid-cols-2 gap-3" role="radiogroup">
        {options.map((o) => {
          const sel = value === o.value
          return (
            <button
              key={o.value}
              type="button"
              role="radio"
              aria-checked={sel}
              onClick={() => onChange(o.value)}
              className={`flex h-12 items-center justify-center gap-2 rounded-8 font-semibold ${
                sel ? 'bg-secundaria text-primaria-hover ring-1 ring-primaria' : 'bg-superficie border border-borda-campo text-texto'
              }`}
            >
              {sel && <Check className="size-5" aria-hidden />}
              {o.label}
            </button>
          )
        })}
      </div>
    </fieldset>
  )
}
