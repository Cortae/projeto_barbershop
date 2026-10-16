const asNumber = (value) => Number(value ?? 0)

export function mapAgendamentoResponse(response) {
  if (!response) return null
  const servicos = (response.servicos ?? []).map((servico) => ({
    id: servico.id,
    nome: servico.titulo,
    valor: asNumber(servico.valorUnitario),
    duracaoMinutos: servico.duracao,
  }))

  return {
    id: response.idAgendamento,
    horarioInicio: response.horarioInicio,
    horarioFim: response.horarioFim,
    titulo: response.titulo,
    status: response.status?.toLowerCase(),
    barbeiroId: response.barbeiroId,
    clienteId: response.cliente?.id,
    cliente: response.cliente,
    servicos,
    valor: asNumber(response.pagamento?.valor),
    duracaoMinutos: servicos.reduce((total, servico) => total + servico.duracaoMinutos, 0),
    statusPagamento: response.pagamento?.pago ? 'pago' : 'pendente',
    pagamentoId: response.pagamento?.id,
    formaPagamentoId: response.pagamento?.formaPagamentoId,
    formaPagamento: response.pagamento?.formaPagamento ?? null,
    dataPagamento: response.pagamento?.dataPagamento ?? null,
  }
}

export const mapAgendamentosResponse = (responses = []) => responses.map(mapAgendamentoResponse)
