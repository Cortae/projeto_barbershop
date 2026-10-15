import { useQuery } from '@tanstack/react-query'
import { db, request, nextId, ApiError } from '@/shared/api'
import { timeToMinutes } from '@/shared/lib/date'

export const scheduleKeys = { all: ['schedule'], blocks: ['schedule', 'blocks'], hours: ['schedule', 'hours'] }

export const fetchBlocks = () => request(() => db.bloqueios)
export const fetchWorkingHours = () => request(() => db.expediente)

export const createBlock = (block) =>
  request(() => {
    const [s, e] = [timeToMinutes(block.inicio), timeToMinutes(block.fim)]
    const clash = db.agendamentos.some((a) => {
      if (a.status === 'cancelado' || a.horarioInicio.slice(0, 10) !== block.data) return false
      const as = timeToMinutes(a.horarioInicio.slice(11, 16))
      return as < e && s < as + a.duracaoMinutos
    })
    if (clash) throw new ApiError('conflict', 'Já existe atendimento nesse horário. Remarque ou cancele antes de bloquear.')
    const b = { ...block, id: nextId('b') }
    db.bloqueios.push(b)
    return b
  })
export const saveWorkingHours = (hours) => request(() => Object.assign(db.expediente, hours))

export const useBlocks = () => useQuery({ queryKey: scheduleKeys.blocks, queryFn: fetchBlocks })
export const useWorkingHours = () => useQuery({ queryKey: scheduleKeys.hours, queryFn: fetchWorkingHours })
