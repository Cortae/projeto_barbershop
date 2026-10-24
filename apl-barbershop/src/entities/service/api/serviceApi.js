import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const serviceKeys = { all: ['services'] }

export const fetchServices = () => request(() => [...db.servicos].sort((a, b) => a.nome.localeCompare(b.nome)))
export const saveService = (data) =>
  request(() => {
    if (data.id) {
      Object.assign(db.servicos.find((s) => s.id === data.id), data)
      return data
    }
    const s = { ...data, id: nextId('s') }
    db.servicos.push(s)
    return s
  })

export const useServices = () => useQuery({ queryKey: serviceKeys.all, queryFn: fetchServices })
