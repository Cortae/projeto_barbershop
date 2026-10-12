import { Card } from '@/shared/ui'
import { formatCurrency, formatDate } from '@/shared/lib/format'

export default function ExpenseCard({ expense }) {
  return (
    <Card className="flex items-center gap-3">
      <div className="min-w-0 flex-1">
        <p className="truncate">{expense.description}</p>
        <p className="texto-corpo-pequeno text-texto-secundario">{formatDate(expense.date)}</p>
      </div>
      <span className="texto-valor">{formatCurrency(expense.amountCents)}</span>
    </Card>
  )
}
