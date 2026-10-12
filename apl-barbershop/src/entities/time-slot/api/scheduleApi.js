import { useQuery } from '@tanstack/react-query'
import { db, request, nextId, ApiError } from '@/shared/api'
import { timeToMinutes } from '@/shared/lib/date'

export const scheduleKeys = { all: ['schedule'], blocks: ['schedule', 'blocks'], hours: ['schedule', 'hours'] }

export const fetchBlocks = () => request(() => db.blocks)
export const fetchWorkingHours = () => request(() => db.workingHours)

export const createBlock = (block) =>
  request(() => {
    const [s, e] = [timeToMinutes(block.start), timeToMinutes(block.end)]
    const clash = db.appointments.some((a) => {
      if (a.status === 'cancelado' || a.startsAt.slice(0, 10) !== block.date) return false
      const as = timeToMinutes(a.startsAt.slice(11, 16))
      return as < e && s < as + a.durationMin
    })
    if (clash) throw new ApiError('conflict', 'Já existe atendimento nesse horário. Remarque ou cancele antes de bloquear.')
    const b = { ...block, id: nextId('b') }
    db.blocks.push(b)
    return b
  })
export const saveWorkingHours = (hours) => request(() => Object.assign(db.workingHours, hours))

export const useBlocks = () => useQuery({ queryKey: scheduleKeys.blocks, queryFn: fetchBlocks })
export const useWorkingHours = () => useQuery({ queryKey: scheduleKeys.hours, queryFn: fetchWorkingHours })
