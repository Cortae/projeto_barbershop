import { Button } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { formatCurrency } from '@/shared/lib/format'
import { markAppointmentPaid } from '@/entities/appointment'

export default function MarkAsPaidButton({ appointment, size = 'sm', full }) {
  const mutation = useAppMutation({
    mutationFn: () => markAppointmentPaid(appointment.id),
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: `Pagamento de ${formatCurrency(appointment.priceCents)} registrado.`,
  })
  return (
    <Button
      variant="secondary"
      size={size}
      full={full}
      loading={mutation.isPending}
      loadingLabel="Salvando..."
      onClick={(e) => {
        e.stopPropagation()
        mutation.mutate()
      }}
    >
      Marcar como pago
    </Button>
  )
}
