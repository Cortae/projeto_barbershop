import { Search } from 'lucide-react'

export default function ClientSearch({ value, onChange, disabled }) {
  return (
    <div className="relative">
      <label htmlFor="busca-clientes" className="sr-only">Buscar por nome ou telefone</label>
      <Search className="pointer-events-none absolute left-3 top-1/2 size-5 -translate-y-1/2 text-texto-secundario" aria-hidden />
      <input
        id="busca-clientes"
        type="search"
        placeholder="Buscar por nome ou telefone"
        value={value}
        disabled={disabled}
        onChange={(e) => onChange(e.target.value)}
        className="h-12 w-full rounded-8 border border-borda-campo bg-superficie-elevada pl-10 pr-4 text-texto placeholder:text-texto-secundario focus:border-2 focus:border-primaria focus:outline-none"
      />
    </div>
  )
}
