import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const expenseKeys = { all: ['expenses'], month: (m) => ['expenses', m] }

export const fetchExpenses = (month) =>
  request(() => db.despesas.filter((e) => e.data.startsWith(month)).sort((a, b) => b.data.localeCompare(a.data)))
export const createExpense = (data) =>
  request(() => {
    const e = { ...data, id: nextId('e') }
    db.despesas.push(e)
    return e
  })

export const useExpenses = (month) => useQuery({ queryKey: expenseKeys.month(month), queryFn: () => fetchExpenses(month) })
