import { useQuery } from "@tanstack/react-query"
import { apiRequest } from "@/shared/api"

export const expenseKeys = { all: ["expenses"], month: (m) => ["expenses", m] }

const mapExpense = (expense) => ({
  id: expense.idDespesa,
  descricao: expense.descricao,
  valorCentavos: Math.round(Number(expense.valor) * 100),
  data: expense.dataDespesa,
  usuario: expense.usuario,
})

export const fetchExpenses = (month) =>
  apiRequest(`/despesas?mes=${encodeURIComponent(month)}`).then((response) =>
    response.itens.map(mapExpense).sort((a, b) => b.data.localeCompare(a.data)),
  )

export const createExpense = (data) =>
  apiRequest("/despesas", {
    method: "POST",
    body: JSON.stringify({
      usuarioId: data.usuarioId ?? 2,
      descricao: data.descricao,
      valor: data.valorCentavos / 100,
      dataDespesa: data.data,
    }),
  }).then(mapExpense)

export const useExpenses = (month) =>
  useQuery({
    queryKey: expenseKeys.month(month),
    queryFn: () => fetchExpenses(month),
  })
