import { useQuery } from '@tanstack/react-query'
import { apiRequest, db, request } from '@/shared/api'

export const scheduleKeys = {
  all: ['schedule'],
  blocks: (from, to, barberId = 2) => ['schedule', 'blocks', from, to, barberId],
  hours: ['schedule', 'hours'],
}

const mapBlock = (block) => ({
  id: block.id,
  barbeiroId: block.usuarioId,
  data: block.inicio.slice(0, 10),
  inicio: block.inicio.slice(11, 16),
  fim: block.fim.slice(11, 16),
  motivo: block.motivo,
})

export const fetchBlocks = (from, to, barberId = 2) => apiRequest(
  `/bloqueios?barbeiroId=${barberId}&inicio=${encodeURIComponent(`${from}T00:00:00`)}&fim=${encodeURIComponent(`${to}T23:59:59`)}`,
).then((items) => items.map(mapBlock))

// O expediente continua local até a API específica ser criada.
export const fetchWorkingHours = () => request(() => db.expediente)

export const createBlock = (block) => apiRequest('/bloqueios', {
  method: 'POST',
  body: JSON.stringify({
    usuarioId: block.barbeiroId ?? 2,
    inicio: `${block.data}T${block.inicio}:00`,
    fim: `${block.data}T${block.fim}:00`,
    motivo: block.motivo || null,
  }),
}).then(mapBlock)

export const deleteBlock = (id) => apiRequest(`/bloqueios/${Number(id)}`, { method: 'DELETE' })
export const saveWorkingHours = (hours) => request(() => Object.assign(db.expediente, hours))

export const useBlocks = (from, to, barberId = 2) => useQuery({
  queryKey: scheduleKeys.blocks(from, to, barberId),
  queryFn: () => fetchBlocks(from, to, barberId),
  enabled: Boolean(from && to),
})

export const useWorkingHours = () => useQuery({ queryKey: scheduleKeys.hours, queryFn: fetchWorkingHours })
