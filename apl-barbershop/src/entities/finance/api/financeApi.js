import { useQuery } from '@tanstack/react-query'
import { fetchAppointments } from '@/entities/appointment'
import { fetchExpenses } from '@/entities/expense'

export const financeKeys = { all: ['finance'], month: (month) => ['finance', month] }

const monthEnd = (month) => {
  const [year, number] = month.split('-').map(Number)
  const day = new Date(Date.UTC(year, number, 0)).getUTCDate()
  return `${month}-${String(day).padStart(2, '0')}`
}

export async function fetchFinanceSummary(month) {
  const [appointments, expenses] = await Promise.all([
    fetchAppointments(`${month}-01`, monthEnd(month)),
    fetchExpenses(month),
  ])
  const done = appointments.filter((appointment) => appointment.status === 'concluido')
  const gross = done.reduce((total, appointment) => total + appointment.valor, 0)
  const expensesValue = expenses.reduce((total, expense) => total + expense.valorCentavos / 100, 0)
  const pending = done.filter((appointment) => appointment.statusPagamento === 'pendente')
  const weekOf = (iso) => Math.min(4, Math.ceil(Number(iso.slice(8, 10)) / 7)) - 1
  const weeks = Array.from({ length: 4 }, () => ({ valorBruto: 0, despesas: 0 }))
  done.forEach((appointment) => { weeks[weekOf(appointment.horarioInicio)].valorBruto += appointment.valor })
  expenses.forEach((expense) => { weeks[weekOf(expense.data)].despesas += expense.valorCentavos / 100 })

  const services = new Map()
  done.forEach((appointment) => appointment.servicos.forEach((service) => {
    const current = services.get(service.id) ?? { servicoId: service.id, nome: service.nome, total: 0 }
    current.total += service.valor
    services.set(service.id, current)
  }))
  const byService = [...services.values()]
    .sort((a, b) => b.total - a.total)
    .map((service) => ({ ...service, percentual: gross ? Math.round((service.total / gross) * 100) : 0 }))

  return {
    valorBruto: gross,
    despesas: expensesValue,
    valorLiquido: gross - expensesValue,
    valorAReceber: pending.reduce((total, appointment) => total + appointment.valor, 0),
    semanas: weeks.map((week, index) => ({
      rotulo: `Sem ${index + 1}`,
      valorBruto: week.valorBruto,
      valorLiquido: Math.max(0, week.valorBruto - week.despesas),
    })),
    porServico: byService,
    pendentes: pending.sort((a, b) => b.horarioInicio.localeCompare(a.horarioInicio)),
  }
}

export const useFinanceSummary = (month) => useQuery({
  queryKey: financeKeys.month(month),
  queryFn: () => fetchFinanceSummary(month),
})
