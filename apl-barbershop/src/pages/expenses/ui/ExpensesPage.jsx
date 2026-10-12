import { useState } from 'react'
import { AppLayout } from '@/widgets/app-layout'
import { ExpensesList } from '@/widgets/expenses-list'
import { ExpenseFormSheet } from '@/features/expense/create-expense'
import { MonthSelector, useMonthParam } from '@/features/finance/select-finance-period'
import { useExpenses } from '@/entities/expense'
import { SkeletonList, ErrorState } from '@/shared/ui'
import { formatCurrency } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

export default function ExpensesPage() {
  const [month] = useMonthParam()
  const [open, setOpen] = useState(false)
  const { data, isLoading, isError, refetch } = useExpenses(month)
  const total = data?.reduce((s, e) => s + e.amountCents, 0) ?? 0
  return (
    <AppLayout title="Despesas" back={`${ROUTES.finance}?periodo=${month}`} primaryAction={{ label: 'Nova despesa', onClick: () => setOpen(true) }}>
      <div className="mx-auto flex max-w-2xl flex-col gap-4">
        <MonthSelector disabled={isLoading} />
        <div className="flex items-baseline justify-between rounded-12 border border-borda bg-superficie p-4">
          <span className="texto-label text-texto-secundario">Total do mês</span>
          <span className="texto-kpi">{formatCurrency(total)}</span>
        </div>
        {isLoading && <SkeletonList rows={3} height="h-16" />}
        {isError && <ErrorState title="Não foi possível carregar as despesas" onRetry={refetch} />}
        {data && <ExpensesList expenses={data} onCreate={() => setOpen(true)} />}
      </div>
      {open && <ExpenseFormSheet open onClose={() => setOpen(false)} />}
    </AppLayout>
  )
}
