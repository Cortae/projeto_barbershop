import test from 'node:test'
import assert from 'node:assert/strict'
import { mapAgendamentoResponse, mapAgendamentosResponse } from './appointmentMapper.js'

const response = {
  idAgendamento: 15,
  horarioInicio: '2026-10-15T14:00:00',
  horarioFim: '2026-10-15T14:50:00',
  titulo: 'Carlos Souza - Duração: 50min',
  status: 'AGENDADO',
  barbeiroId: 2,
  cliente: { id: 3, nome: 'Carlos Souza' },
  servicos: [
    { id: 1, titulo: 'Corte', valorUnitario: 45, duracao: 30 },
    { id: 2, titulo: 'Barba', valorUnitario: 30, duracao: 20 },
  ],
  pagamento: { id: 9, valor: 75, pago: false, formaPagamentoId: null, formaPagamento: null },
}

test('mapeia status, reais, pagamento e todos os serviços', () => {
  const appointment = mapAgendamentoResponse(response)
  assert.equal(appointment.id, 15)
  assert.equal(appointment.status, 'agendado')
  assert.equal(appointment.valor, 75)
  assert.equal(appointment.statusPagamento, 'pendente')
  assert.equal(appointment.duracaoMinutos, 50)
  assert.deepEqual(appointment.servicos.map((service) => service.id), [1, 2])
})

test('mapeia listas vazias e listas com itens', () => {
  assert.deepEqual(mapAgendamentosResponse([]), [])
  assert.equal(mapAgendamentosResponse([response]).length, 1)
})
