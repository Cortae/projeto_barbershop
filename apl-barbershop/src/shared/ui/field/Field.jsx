import { useId } from 'react'
import { AlertCircle } from 'lucide-react'

const base =
  'w-full rounded-8 bg-superficie-elevada text-texto placeholder:text-texto-secundario border border-borda-campo hover:border-texto-secundario focus:outline-none focus:border-2 focus:border-primaria disabled:bg-superficie disabled:text-texto-secundario'

/** Rótulo sempre visível + erro com ícone e texto (RNF02). */
export default function Field({ label, required, error, hint, children }) {
  const id = useId()
  const errorId = `${id}-erro`
  return (
    <div className="flex flex-col gap-2">
      <label htmlFor={id} className="texto-label text-texto">
        {label}
        {required && <span aria-hidden> *</span>}
      </label>
      {children({
        id,
        'aria-invalid': error ? true : undefined,
        'aria-describedby': error ? errorId : undefined,
        className: `${base} ${error ? 'border-2 border-erro' : ''}`,
      })}
      {hint && !error && <p className="texto-corpo-pequeno text-texto-secundario">{hint}</p>}
      {error && (
        <p id={errorId} className="texto-corpo-pequeno flex items-center gap-1 text-erro">
          <AlertCircle className="size-4" aria-hidden /> {error}
        </p>
      )}
    </div>
  )
}

export function TextField({ label, required, error, hint, className = '', ...props }) {
  return (
    <Field label={label} required={required} error={error} hint={hint}>
      {(p) => <input {...p} {...props} className={`${p.className} h-12 px-4 ${className}`} />}
    </Field>
  )
}

export function SelectField({ label, required, error, placeholder, options, ...props }) {
  return (
    <Field label={label} required={required} error={error}>
      {(p) => (
        <select {...p} {...props} className={`${p.className} h-12 px-4`}>
          {placeholder && <option value="">{placeholder}</option>}
          {options.map((o) => (
            <option key={o.value} value={o.value} disabled={o.disabled}>
              {o.label}
            </option>
          ))}
        </select>
      )}
    </Field>
  )
}

export function TextAreaField({ label, error, ...props }) {
  return (
    <Field label={label} error={error}>
      {(p) => <textarea rows={3} {...p} {...props} className={`${p.className} px-4 py-3`} />}
    </Field>
  )
}
