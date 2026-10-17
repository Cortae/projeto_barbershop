import { useQuery } from '@tanstack/react-query'
import { ApiError, apiRequest } from '@/shared/api'
import { mapAgendamentoResponse, mapAgendamentosResponse } from './appointmentMapper'

export const appointmentKeys = {
  all: ['appointments'],
  range: (from, to) => ['appointments', 'range', from, to],
  detail: (id) => ['appointments', Number(id)],
  byClient: (clientId) => ['appointments', 'client', Number(clientId)],
  byBarber: (barberId) => ['appointments', 'barber', Number(barberId)],
}

const positiveInteger = (value, field) => {
  const parsed = Number(value)
  if (!Number.isInteger(parsed) || parsed <= 0) {
    throw new ApiError('invalid_payload', `${field} deve ser um número inteiro positivo.`)
  }
  return parsed
}

const serviceIds = (values) => {
  if (!Array.isArray(values) || !values.length) {
    throw new ApiError('invalid_payload', 'Selecione ao menos um serviço.')
  }
  return values.map((value) => positiveInteger(value, 'servicos'))
}

const mapOne = (request) => request.then(mapAgendamentoResponse)
const mapMany = (request) => request.then(mapAgendamentosResponse)

export const fetchAppointments = (from, to) =>
  mapMany(apiRequest(`/agendamentos/periodo?de=${encodeURIComponent(from)}&ate=${encodeURIComponent(to)}`))

export const fetchAppointment = (id) =>
  mapOne(apiRequest(`/agendamentos/${positiveInteger(id, 'idAgendamento')}`))

export const fetchClientAppointments = (clientId) =>
  mapMany(apiRequest(`/agendamentos/cliente/${positiveInteger(clientId, 'clienteId')}`))

export const buscarPorBarbeiro = (barbeiroId) =>
  mapMany(apiRequest(`/agendamentos/barbeiro/${positiveInteger(barbeiroId, 'barbeiroId')}`))

export const createAppointment = ({ barbeiroId = 2, clienteId, servicos, horarioInicio }) =>
  mapOne(apiRequest('/agendamentos', {
    method: 'POST',
    body: JSON.stringify({
      barbeiro: positiveInteger(barbeiroId, 'barbeiro'),
      cliente: positiveInteger(clienteId, 'cliente'),
      servicos: serviceIds(servicos),
      inicio: horarioInicio,
    }),
  }))

export const editAppointment = ({ id, clienteId, servicos, horarioInicio }) =>
  mapOne(apiRequest(`/agendamentos/${positiveInteger(id, 'idAgendamento')}`, {
    method: 'PUT',
    body: JSON.stringify({
      cliente: positiveInteger(clienteId, 'cliente'),
      servicos: serviceIds(servicos),
      inicio: horarioInicio,
    }),
  }))

export const saveAppointment = (appointment) =>
  appointment.id ? editAppointment(appointment) : createAppointment(appointment)

export const cancelAppointment = (id) =>
  mapOne(apiRequest(`/agendamentos/cancelar/${positiveInteger(id, 'idAgendamento')}`, { method: 'PATCH' }))

export const completeAppointment = (id, formaPagamentoId = null) =>
  mapOne(apiRequest(`/agendamentos/concluir/${positiveInteger(id, 'idAgendamento')}`, {
    method: 'PATCH',
    ...(formaPagamentoId
      ? { body: JSON.stringify({ formaPagamentoId: positiveInteger(formaPagamentoId, 'formaPagamentoId') }) }
      : {}),
  }))

export const markAppointmentPaid = (id, formaPagamentoId) =>
  mapOne(apiRequest('/agendamentos/pagar', {
    method: 'PATCH',
    body: JSON.stringify({
      idAgendamento: positiveInteger(id, 'idAgendamento'),
      formaPagamentoId: positiveInteger(formaPagamentoId, 'formaPagamentoId'),
    }),
  }))

export const remarcarAgendamento = ({ idAgendamento, inicio }) =>
  mapOne(apiRequest('/agendamentos/horario', {
    method: 'PATCH',
    body: JSON.stringify({ idAgendamento: positiveInteger(idAgendamento, 'idAgendamento'), inicio }),
  }))

export const atualizarServicosAgendamento = ({ idAgendamento, servicos }) =>
  mapOne(apiRequest('/agendamentos/servicos', {
    method: 'PATCH',
    body: JSON.stringify({
      idAgendamento: positiveInteger(idAgendamento, 'idAgendamento'),
      servicos: serviceIds(servicos),
    }),
  }))

export const useAppointments = (from, to) => useQuery({
  queryKey: appointmentKeys.range(from, to),
  queryFn: () => fetchAppointments(from, to),
  enabled: Boolean(from && to),
})

export const useAppointment = (id) => useQuery({
  queryKey: appointmentKeys.detail(id),
  queryFn: () => fetchAppointment(id),
  enabled: Boolean(id),
})

export const useClientAppointments = (clientId) => useQuery({
  queryKey: appointmentKeys.byClient(clientId),
  queryFn: () => fetchClientAppointments(clientId),
  enabled: Boolean(clientId),
})
