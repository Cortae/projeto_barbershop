import { AppLayout } from '@/widgets/app-layout'
import { FinanceKpis } from '@/widgets/finance-kpis'
import { ProfitChart, RevenueByService } from '@/widgets/revenue-by-service-chart'
import { PendingPaymentsList } from '@/widgets/pending-payments-list'
import { MonthSelector, useMonthParam } from '@/features/finance/select-finance-period'
import { useFinanceSummary } from '@/entities/finance'
import { Skeleton, ErrorState } from '@/shared/ui'

export default function FinancePage() {
  const [month] = useMonthParam()
  const { data, isLoading, isError, refetch } = useFinanceSummary(month)
  return (
    <AppLayout title="Financeiro">
      <div className="flex flex-col gap-6">
        <div className="lg:max-w-sm"><MonthSelector disabled={isLoading} /></div>
        {isError && <ErrorState title="Não foi possível carregar o financeiro" onRetry={refetch} />}
        {isLoading && (
          <div className="grid grid-cols-2 gap-3 lg:grid-cols-4" aria-busy="true">
            {[0, 1, 2, 3].map((i) => <Skeleton key={i} className="h-28 rounded-12" />)}
            <Skeleton className="col-span-2 h-64 rounded-12 lg:col-span-4" />
          </div>
        )}
        {data && (
          <>
            <FinanceKpis summary={data} />
            <div className="grid gap-6 lg:grid-cols-2">
              <ProfitChart weeks={data.semanas} />
              <RevenueByService items={data.porServico} />
            </div>
            <PendingPaymentsList items={data.pendentes} />
          </>
        )}
      </div>
    </AppLayout>
  )
}
