import { useQuery } from '@tanstack/react-query'
import { db, request } from '@/shared/api'

export const financeKeys = { all: ['finance'], month: (m) => ['finance', m] }

export const fetchFinanceSummary = (month) =>
  request(() => {
    const done = db.agendamentos.filter((a) => a.status === 'concluido' && a.horarioInicio.startsWith(month))
    const expenses = db.despesas.filter((e) => e.data.startsWith(month))
    const grossCents = done.reduce((s, a) => s + a.valorCentavos, 0)
    const expensesCents = expenses.reduce((s, e) => s + e.valorCentavos, 0)
    const pending = done.filter((a) => a.statusPagamento === 'pendente')
    const weekOf = (iso) => Math.min(4, Math.ceil(Number(iso.slice(8, 10)) / 7)) - 1
    const weeks = Array.from({ length: 4 }, () => ({ valorBrutoCentavos: 0, despesasCentavos: 0 }))
    done.forEach((a) => (weeks[weekOf(a.horarioInicio)].valorBrutoCentavos += a.valorCentavos))
    expenses.forEach((e) => (weeks[weekOf(e.data)].despesasCentavos += e.valorCentavos))
    const byService = db.servicos
      .map((s) => ({ servicoId: s.id, nome: s.nome, totalCentavos: done.filter((a) => a.servicoId === s.id).reduce((t, a) => t + a.valorCentavos, 0) }))
      .filter((s) => s.totalCentavos > 0)
      .sort((a, b) => b.totalCentavos - a.totalCentavos)
      .map((s) => ({ ...s, percentual: Math.round((s.totalCentavos / grossCents) * 100) }))
    return {
      valorBrutoCentavos: grossCents,
      despesasCentavos: expensesCents,
      valorLiquidoCentavos: grossCents - expensesCents,
      valorAReceberCentavos: pending.reduce((s, a) => s + a.valorCentavos, 0),
      semanas: weeks.map((w, i) => ({ rotulo: `Sem ${i + 1}`, valorBrutoCentavos: w.valorBrutoCentavos, valorLiquidoCentavos: Math.max(0, w.valorBrutoCentavos - w.despesasCentavos) })),
      porServico: byService,
      pendentes: pending
        .sort((a, b) => b.horarioInicio.localeCompare(a.horarioInicio))
        .map((a) => ({ ...a, cliente: db.clientes.find((c) => c.id === a.clienteId), servico: db.servicos.find((s) => s.id === a.servicoId) })),
    }
  })

export const useFinanceSummary = (month) => useQuery({ queryKey: financeKeys.month(month), queryFn: () => fetchFinanceSummary(month) })
