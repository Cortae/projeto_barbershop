import { useQuery } from '@tanstack/react-query'
import { apiRequest } from '@/shared/api'

export const serviceKeys = { all: ['services'] }

const mapService = (service) => ({
  id: service.id,
  nome: service.titulo,
  valor: Number(service.valor ?? service.valorAtual),
  duracaoMinutos: service.duracao ?? service.duracaoAtual,
  ativo: service.ativo,
})

export const fetchServices = () =>
  apiRequest('/servicos?ativo=true').then((items) => items.map(mapService).filter((service) => service.ativo))
export const saveService = (service) => apiRequest(service.id ? `/servicos/${Number(service.id)}` : '/servicos', {
  method: service.id ? 'PUT' : 'POST',
  body: JSON.stringify({
    titulo: service.nome,
    valorAtual: service.valor,
    duracaoAtual: service.duracaoMinutos,
  }),
}).then(mapService)
export const deactivateService = (id) => apiRequest(`/servicos/${Number(id)}/ativo`, {
  method: 'PATCH',
  body: JSON.stringify({ ativo: false }),
}).then(mapService)

export const useServices = () => useQuery({ queryKey: serviceKeys.all, queryFn: fetchServices })
