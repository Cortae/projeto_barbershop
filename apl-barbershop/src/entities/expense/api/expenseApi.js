import { useQuery } from '@tanstack/react-query'
import { db, request, nextId } from '@/shared/api'

export const expenseKeys = { all: ['expenses'], month: (m) => ['expenses', m] }

export const fetchExpenses = (month) =>
  request(() => db.expenses.filter((e) => e.date.startsWith(month)).sort((a, b) => b.date.localeCompare(a.date)))
export const createExpense = (data) =>
  request(() => {
    const e = { ...data, id: nextId('e') }
    db.expenses.push(e)
    return e
  })

export const useExpenses = (month) => useQuery({ queryKey: expenseKeys.month(month), queryFn: () => fetchExpenses(month) })
