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
  cliente: db.clientes.find((c) => c.id === a.clienteId),
  servico: db.servicos.find((s) => s.id === a.servicoId),
})

/** Intervalo de datas inclusivo (YYYY-MM-DD). */
export const fetchAppointments = (from, to) =>
  request(() =>
    db.agendamentos
      .filter((a) => a.status !== 'cancelado' && a.horarioInicio.slice(0, 10) >= from && a.horarioInicio.slice(0, 10) <= to)
      .sort((a, b) => a.horarioInicio.localeCompare(b.horarioInicio))
      .map(withRefs),
  )
export const fetchAppointment = (id) => request(() => withRefs(db.agendamentos.find((a) => a.id === id)))
export const fetchClientAppointments = (clientId) =>
  request(() => db.agendamentos.filter((a) => a.clienteId === clientId && a.status === 'concluido').sort((a, b) => b.horarioInicio.localeCompare(a.horarioInicio)).map(withRefs))

export const saveAppointment = (data) =>
  request(() => {
    const service = db.servicos.find((s) => s.id === data.servicoId)
    const payload = { ...data, valorCentavos: service.valorCentavos, duracaoMinutos: service.duracaoMinutos }
    if (findConflict({ ...payload, ignorarId: data.id }, db.agendamentos, db.bloqueios))
      throw new ApiError('conflict', 'Esse horário já está ocupado. Escolha outro horário livre.')
    if (data.id) {
      Object.assign(db.agendamentos.find((a) => a.id === data.id), payload)
      return withRefs(payload)
    }
    const a = { ...payload, id: nextId('a'), status: 'agendado' }
    db.agendamentos.push(a)
    return withRefs(a)
  })

const patch = (id, changes) =>
  request(() => {
    const a = db.agendamentos.find((x) => x.id === id)
    Object.assign(a, changes)
    return withRefs(a)
  })
export const cancelAppointment = (id) => patch(id, { status: 'cancelado' })
export const completeAppointment = (id, { statusPagamento, formaPagamento }) =>
  patch(id, { status: 'concluido', statusPagamento, formaPagamento: statusPagamento === 'pago' ? formaPagamento : undefined })
export const markAppointmentPaid = (id) => patch(id, { statusPagamento: 'pago' })

export const useAppointments = (from, to) => useQuery({ queryKey: appointmentKeys.range(from, to), queryFn: () => fetchAppointments(from, to) })
export const useAppointment = (id) => useQuery({ queryKey: appointmentKeys.detail(id), queryFn: () => fetchAppointment(id), enabled: !!id })
export const useClientAppointments = (clientId) =>
  useQuery({ queryKey: appointmentKeys.byClient(clientId), queryFn: () => fetchClientAppointments(clientId), enabled: !!clientId })
