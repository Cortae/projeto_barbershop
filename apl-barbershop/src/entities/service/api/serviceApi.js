import { useQuery } from '@tanstack/react-query'
import { apiRequest } from '@/shared/api'

export const serviceKeys = { all: ['services'] }

const mapService = (service) => ({
  id: service.id,
  nome: service.titulo,
  valor: Number(service.valor ?? service.valorAtual),
  duracaoMinutos: service.duracao ?? service.duracaoAtual,
})

export const fetchServices = () => apiRequest('/servicos').then((items) => items.map(mapService))
export const saveService = (service) => apiRequest(service.id ? `/servicos/${Number(service.id)}` : '/servicos', {
  method: service.id ? 'PUT' : 'POST',
  body: JSON.stringify({
    titulo: service.nome,
    valorAtual: service.valor,
    duracaoAtual: service.duracaoMinutos,
  }),
}).then(mapService)

export const useServices = () => useQuery({ queryKey: serviceKeys.all, queryFn: fetchServices })
