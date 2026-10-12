import { Scissors } from 'lucide-react'

export default function Logo({ large }) {
  return (
    <span className={`inline-flex items-center gap-2 font-bold tracking-tight ${large ? 'text-2xl' : 'text-lg'}`}>
      <span className={`flex items-center justify-center rounded-8 bg-primaria text-fundo ${large ? 'size-12' : 'size-8'}`}>
        <Scissors className={large ? 'size-6' : 'size-4'} aria-hidden />
      </span>
      <span>
        JR <span className="text-primaria">Barbershop</span>
      </span>
    </span>
  )
}
