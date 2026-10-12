import { Receipt } from 'lucide-react'
import { Button, EmptyState } from '@/shared/ui'
import { ExpenseCard } from '@/entities/expense'

export default function ExpensesList({ expenses, onCreate }) {
  if (expenses.length === 0)
    return (
      <EmptyState
        icon={Receipt}
        title="Nenhuma despesa lançada"
        text="Lance suas despesas para ver o lucro líquido real do mês."
        action={<Button onClick={onCreate}>Nova despesa</Button>}
      />
    )
  return (
    <ul className="flex flex-col gap-3">
      {expenses.map((e) => <li key={e.id}><ExpenseCard expense={e} /></li>)}
    </ul>
  )
}
