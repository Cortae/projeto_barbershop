import { useId, useState } from 'react'
import { Search, AlertCircle } from 'lucide-react'

export default function ClientCombobox({ clients, value, onChange, error, disabled }) {
  const id = useId()
  const selected = clients.find((c) => c.id === value)
  const [query, setQuery] = useState(selected?.name ?? '')
  const [open, setOpen] = useState(false)
  const q = query.trim().toLowerCase()
  const matches = clients.filter((c) => c.name.toLowerCase().includes(q) || c.phone.includes(q)).slice(0, 6)

  return (
    <div className="relative flex flex-col gap-2">
      <label htmlFor={id} className="texto-label">Cliente *</label>
      <div className="relative">
        <Search className="pointer-events-none absolute left-3 top-1/2 size-5 -translate-y-1/2 text-texto-secundario" aria-hidden />
        <input
          id={id}
          role="combobox"
          aria-expanded={open}
          aria-controls={`${id}-lista`}
          aria-invalid={error ? true : undefined}
          disabled={disabled}
          placeholder="Nome do cliente"
          value={query}
          autoComplete="off"
          onFocus={() => setOpen(true)}
          onBlur={() => setTimeout(() => setOpen(false), 150)}
          onChange={(e) => {
            setQuery(e.target.value)
            setOpen(true)
            onChange('')
          }}
          className={`h-12 w-full rounded-8 bg-superficie-elevada pl-10 pr-4 text-texto placeholder:text-texto-secundario focus:outline-none ${
            error ? 'border-2 border-erro' : 'border border-borda-campo focus:border-2 focus:border-primaria'
          }`}
        />
      </div>
      {open && matches.length > 0 && (
        <ul id={`${id}-lista`} role="listbox" className="absolute top-full z-20 mt-1 w-full overflow-hidden rounded-8 border border-borda-campo bg-superficie-elevada">
          {matches.map((c) => (
            <li key={c.id} role="option" aria-selected={c.id === value}>
              <button
                type="button"
                className="flex min-h-11 w-full items-center justify-between px-4 text-left hover:bg-superficie"
                onMouseDown={(e) => e.preventDefault()}
                onClick={() => {
                  onChange(c.id)
                  setQuery(c.name)
                  setOpen(false)
                }}
              >
                <span>{c.name}</span>
                <span className="texto-corpo-pequeno text-texto-secundario">{c.phone}</span>
              </button>
            </li>
          ))}
        </ul>
      )}
      {error && (
        <p className="texto-corpo-pequeno flex items-center gap-1 text-erro">
          <AlertCircle className="size-4" aria-hidden /> {error}
        </p>
      )}
    </div>
  )
}
