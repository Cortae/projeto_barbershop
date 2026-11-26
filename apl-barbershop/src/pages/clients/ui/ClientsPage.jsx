import { useState } from 'react'
import { useNavigate } from 'react-router'
import { ChevronRight, Users, SearchX } from 'lucide-react'
import { AppLayout } from '@/widgets/app-layout'
import { ClientSearch, filterClients } from '@/features/client/search-clients'
import { useClients, ClientCard } from '@/entities/client'
import { PaymentBadge } from '@/entities/payment'
import { Button, EmptyState, ErrorState, SkeletonList } from '@/shared/ui'
import { formatDate } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

export default function ClientsPage() {
  const navigate = useNavigate()
  const [query, setQuery] = useState('')
  const { data, isLoading, isError, refetch } = useClients()
  const list = data ? filterClients(data, query) : []
  const open = (id) => navigate(ROUTES.client(id))

  return (
    <AppLayout title="Clientes" primaryAction={{ label: 'Novo cliente', onClick: () => navigate(ROUTES.clientNew) }}>
      <div className="flex flex-col gap-4">
        {data?.length !== 0 && <ClientSearch value={query} onChange={setQuery} disabled={isLoading} />}
        {query && data && <p className="texto-corpo-pequeno text-texto-secundario" aria-live="polite">{list.length} {list.length === 1 ? 'resultado' : 'resultados'}</p>}
        {isLoading && <SkeletonList rows={4} />}
        {isError && <ErrorState title="Não foi possível carregar os clientes" onRetry={refetch} />}
        {data?.length === 0 && (
          <EmptyState icon={Users} title="Nenhum cliente ainda" text="Cadastre seu primeiro cliente para começar a agendar cortes." action={<Button onClick={() => navigate(ROUTES.clientNew)}>Cadastrar cliente</Button>} />
        )}
        {data?.length > 0 && list.length === 0 && (
          <EmptyState icon={SearchX} title="Nenhum cliente encontrado" text="Confira o nome ou telefone, ou cadastre um novo cliente." action={<Button onClick={() => navigate(ROUTES.clientNew)}>Cadastrar cliente</Button>} />
        )}
        {list.length > 0 && (
          <>
            <ul className="flex flex-col gap-3 lg:hidden">
              {list.map((c) => <li key={c.id}><ClientCard client={c} onClick={() => open(c.id)} /></li>)}
            </ul>
            <table className="hidden w-full overflow-hidden rounded-12 border border-borda lg:table">
              <thead className="bg-superficie-elevada text-left">
                <tr className="texto-label text-texto-secundario">
                  <th className="px-4 py-3 font-medium">Nome</th>
                  <th className="px-4 py-3 font-medium">Telefone</th>
                  <th className="px-4 py-3 font-medium">Último corte</th>
                  <th className="px-4 py-3 font-medium">Pendência</th>
                  <th className="w-12"><span className="sr-only">Abrir</span></th>
                </tr>
              </thead>
              <tbody className="divide-y divide-borda bg-superficie">
                {list.map((c) => (
                  <tr key={c.id} onClick={() => open(c.id)} className="cursor-pointer hover:bg-superficie-elevada">
                    <td className="px-4 py-3">
                      <button className="font-medium hover:underline" onClick={() => open(c.id)}>{c.nome}</button>
                    </td>
                    <td className="px-4 py-3 tabular-nums text-texto-secundario">{c.telefone}</td>
                    <td className="px-4 py-3 tabular-nums text-texto-secundario">{c.ultimaVisitaEm ? formatDate(c.ultimaVisitaEm) : '—'}</td>
                    <td className="px-4 py-3">{c.valorPendente > 0 ? <PaymentBadge status="pendente" /> : <span className="text-texto-secundario">—</span>}</td>
                    <td className="pr-4"><ChevronRight className="size-5 text-texto-secundario" aria-hidden /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}
      </div>
    </AppLayout>
  )
}
