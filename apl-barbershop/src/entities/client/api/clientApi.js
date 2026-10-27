import { useQuery } from '@tanstack/react-query'
import { apiRequest } from '@/shared/api'

export const clientKeys = { all: ['clients'], detail: (id) => ['clients', Number(id)] }

const mapClient = (client) => ({
  ...client,
  telefone: client.telefone ?? client.celular ?? '',
  dataNascimento: client.nascimento ?? '',
  ultimaVisitaEm: client.ultimaVisitaEm ?? client.ultimoCorte ?? null,
  valorPendente: Number(client.valorPendente ?? client.totalPendente ?? 0),
})

const payload = (client) => ({
  nome: client.nome,
  apelido: client.apelido || null,
  celular: client.telefone.replace(/\D/g, ''),
  nascimento: client.dataNascimento || null,
})

export const fetchClients = () => apiRequest('/clientes').then((items) => items.map(mapClient))
export const fetchClient = (id) => apiRequest(`/clientes/${Number(id)}`).then(mapClient)
export const saveClient = (client) => apiRequest(client.id ? `/clientes/${Number(client.id)}` : '/clientes', {
  method: client.id ? 'PUT' : 'POST',
  body: JSON.stringify(payload(client)),
}).then(mapClient)
export const deleteClient = (id) => apiRequest(`/clientes/${Number(id)}`, { method: 'DELETE' })

export const useClients = () => useQuery({ queryKey: clientKeys.all, queryFn: fetchClients })
export const useClient = (id) => useQuery({
  queryKey: clientKeys.detail(id),
  queryFn: () => fetchClient(id),
  enabled: Boolean(id),
})
