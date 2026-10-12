import { House, CalendarDays, Users, Wallet, Scissors } from 'lucide-react'
import { ROUTES } from '@/shared/config'

export const NAV_ITEMS = [
  { to: ROUTES.home, label: 'Início', icon: House, match: (p) => p === '/' },
  { to: ROUTES.schedule, label: 'Agenda', icon: CalendarDays, match: (p) => p.startsWith('/agenda') },
  { to: ROUTES.clients, label: 'Clientes', icon: Users, match: (p) => p.startsWith('/clientes') },
  { to: ROUTES.finance, label: 'Financeiro', icon: Wallet, match: (p) => p.startsWith('/financeiro') },
  { to: ROUTES.services, label: 'Serviços', icon: Scissors, match: (p) => p.startsWith('/servicos') },
]
