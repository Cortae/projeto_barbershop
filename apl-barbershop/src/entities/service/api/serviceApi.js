import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const serviceKeys = { all: ['services'] }

export const fetchServices = () => request(() => [...db.services].sort((a, b) => a.name.localeCompare(b.name)))
export const saveService = (data) =>
  request(() => {
    if (data.id) {
      Object.assign(db.services.find((s) => s.id === data.id), data)
      return data
    }
    const s = { ...data, id: nextId('s') }
    db.services.push(s)
    return s
  })

export const useServices = () => useQuery({ queryKey: serviceKeys.all, queryFn: fetchServices })
