import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const clientKeys = { all: ['clients'], detail: (id) => ['clients', id] }

function enrich(c) {
  const done = db.appointments.filter((a) => a.clientId === c.id && a.status === 'concluido')
  const last = done.map((a) => a.startsAt).sort().at(-1)
  const pendingCents = done.filter((a) => a.paymentStatus === 'pendente').reduce((s, a) => s + a.priceCents, 0)
  return { ...c, lastVisitAt: last ?? null, pendingCents }
}

export const fetchClients = () => request(() => db.clients.map(enrich).sort((a, b) => a.name.localeCompare(b.name)))
export const fetchClient = (id) => request(() => enrich(db.clients.find((c) => c.id === id)))
export const saveClient = (data) =>
  request(() => {
    if (data.id) {
      Object.assign(db.clients.find((c) => c.id === data.id), data)
      return data
    }
    const c = { ...data, id: nextId('c') }
    db.clients.push(c)
    return c
  })

export const useClients = () => useQuery({ queryKey: clientKeys.all, queryFn: fetchClients })
export const useClient = (id) => useQuery({ queryKey: clientKeys.detail(id), queryFn: () => fetchClient(id), enabled: !!id })
