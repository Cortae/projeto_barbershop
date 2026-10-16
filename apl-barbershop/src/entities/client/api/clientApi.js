import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const clientKeys = { all: ['clients'], detail: (id) => ['clients', id] }

function enrich(c) {
  const done = db.agendamentos.filter((a) => a.clienteId === c.id && a.status === 'concluido')
  const last = done.map((a) => a.horarioInicio).sort().at(-1)
  const pendingCents = done.filter((a) => a.statusPagamento === 'pendente').reduce((s, a) => s + a.valorCentavos, 0)
  return { ...c, ultimaVisitaEm: last ?? null, valorPendenteCentavos: pendingCents }
}

export const fetchClients = () => request(() => db.clientes.map(enrich).sort((a, b) => a.nome.localeCompare(b.nome)))
export const fetchClient = (id) => request(() => enrich(db.clientes.find((c) => c.id === id)))
export const saveClient = (data) =>
  request(() => {
    if (data.id) {
      Object.assign(db.clientes.find((c) => c.id === data.id), data)
      return data
    }
    const c = { ...data, id: nextId('c') }
    db.clientes.push(c)
    return c
  })

export const useClients = () => useQuery({ queryKey: clientKeys.all, queryFn: fetchClients })
export const useClient = (id) => useQuery({ queryKey: clientKeys.detail(id), queryFn: () => fetchClient(id), enabled: !!id })
