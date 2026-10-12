import { useQuery } from '@tanstack/react-query'
import { db, request } from '@/shared/api'

export const financeKeys = { all: ['finance'], month: (m) => ['finance', m] }

export const fetchFinanceSummary = (month) =>
  request(() => {
    const done = db.appointments.filter((a) => a.status === 'concluido' && a.startsAt.startsWith(month))
    const expenses = db.expenses.filter((e) => e.date.startsWith(month))
    const grossCents = done.reduce((s, a) => s + a.priceCents, 0)
    const expensesCents = expenses.reduce((s, e) => s + e.amountCents, 0)
    const pending = done.filter((a) => a.paymentStatus === 'pendente')
    const weekOf = (iso) => Math.min(4, Math.ceil(Number(iso.slice(8, 10)) / 7)) - 1
    const weeks = Array.from({ length: 4 }, () => ({ grossCents: 0, expensesCents: 0 }))
    done.forEach((a) => (weeks[weekOf(a.startsAt)].grossCents += a.priceCents))
    expenses.forEach((e) => (weeks[weekOf(e.date)].expensesCents += e.amountCents))
    const byService = db.services
      .map((s) => ({ serviceId: s.id, name: s.name, totalCents: done.filter((a) => a.serviceId === s.id).reduce((t, a) => t + a.priceCents, 0) }))
      .filter((s) => s.totalCents > 0)
      .sort((a, b) => b.totalCents - a.totalCents)
      .map((s) => ({ ...s, percent: Math.round((s.totalCents / grossCents) * 100) }))
    return {
      grossCents,
      expensesCents,
      netCents: grossCents - expensesCents,
      receivableCents: pending.reduce((s, a) => s + a.priceCents, 0),
      weeks: weeks.map((w, i) => ({ label: `Sem ${i + 1}`, grossCents: w.grossCents, netCents: Math.max(0, w.grossCents - w.expensesCents) })),
      byService,
      pending: pending
        .sort((a, b) => b.startsAt.localeCompare(a.startsAt))
        .map((a) => ({ ...a, client: db.clients.find((c) => c.id === a.clientId), service: db.services.find((s) => s.id === a.serviceId) })),
    }
  })

export const useFinanceSummary = (month) => useQuery({ queryKey: financeKeys.month(month), queryFn: () => fetchFinanceSummary(month) })
