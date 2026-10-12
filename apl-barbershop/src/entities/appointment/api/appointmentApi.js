import { useQuery } from '@tanstack/react-query'
import { db, request, nextId, ApiError } from '@/shared/api'
import { findConflict } from '../model/conflict'

export const appointmentKeys = {
  all: ['appointments'],
  range: (from, to) => ['appointments', 'range', from, to],
  detail: (id) => ['appointments', id],
  byClient: (clientId) => ['appointments', 'client', clientId],
}

const withRefs = (a) => ({
  ...a,
  client: db.clients.find((c) => c.id === a.clientId),
  service: db.services.find((s) => s.id === a.serviceId),
})

/** Intervalo de datas inclusivo (YYYY-MM-DD). */
export const fetchAppointments = (from, to) =>
  request(() =>
    db.appointments
      .filter((a) => a.status !== 'cancelado' && a.startsAt.slice(0, 10) >= from && a.startsAt.slice(0, 10) <= to)
      .sort((a, b) => a.startsAt.localeCompare(b.startsAt))
      .map(withRefs),
  )
export const fetchAppointment = (id) => request(() => withRefs(db.appointments.find((a) => a.id === id)))
export const fetchClientAppointments = (clientId) =>
  request(() => db.appointments.filter((a) => a.clientId === clientId && a.status === 'concluido').sort((a, b) => b.startsAt.localeCompare(a.startsAt)).map(withRefs))

export const saveAppointment = (data) =>
  request(() => {
    const service = db.services.find((s) => s.id === data.serviceId)
    const payload = { ...data, priceCents: service.priceCents, durationMin: service.durationMin }
    if (findConflict({ ...payload, ignoreId: data.id }, db.appointments, db.blocks))
      throw new ApiError('conflict', 'Esse horário já está ocupado. Escolha outro horário livre.')
    if (data.id) {
      Object.assign(db.appointments.find((a) => a.id === data.id), payload)
      return withRefs(payload)
    }
    const a = { ...payload, id: nextId('a'), status: 'agendado' }
    db.appointments.push(a)
    return withRefs(a)
  })

const patch = (id, changes) =>
  request(() => {
    const a = db.appointments.find((x) => x.id === id)
    Object.assign(a, changes)
    return withRefs(a)
  })
export const cancelAppointment = (id) => patch(id, { status: 'cancelado' })
export const completeAppointment = (id, { paymentStatus, paymentMethod }) =>
  patch(id, { status: 'concluido', paymentStatus, paymentMethod: paymentStatus === 'pago' ? paymentMethod : undefined })
export const markAppointmentPaid = (id) => patch(id, { paymentStatus: 'pago' })

export const useAppointments = (from, to) => useQuery({ queryKey: appointmentKeys.range(from, to), queryFn: () => fetchAppointments(from, to) })
export const useAppointment = (id) => useQuery({ queryKey: appointmentKeys.detail(id), queryFn: () => fetchAppointment(id), enabled: !!id })
export const useClientAppointments = (clientId) =>
  useQuery({ queryKey: appointmentKeys.byClient(clientId), queryFn: () => fetchClientAppointments(clientId), enabled: !!clientId })
