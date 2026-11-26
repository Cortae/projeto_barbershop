export function summarizeAppointments(appointments) {
  return appointments.reduce((summary, appointment) => {
    if (appointment.status === 'concluido' && appointment.statusPagamento === 'pago') {
      summary.received += appointment.valor
    }
    if (
      appointment.status === 'agendado'
      || (appointment.status === 'concluido' && appointment.statusPagamento === 'pendente')
    ) {
      summary.toReceive += appointment.valor
    }
    return summary
  }, { received: 0, toReceive: 0 })
}
