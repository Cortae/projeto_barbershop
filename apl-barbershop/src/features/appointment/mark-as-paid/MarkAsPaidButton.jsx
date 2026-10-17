import { useState } from 'react'
import { BottomSheet, Button, Chip } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { formatMoney } from '@/shared/lib/format'
import { markAppointmentPaid } from '@/entities/appointment'
import { usePaymentMethods } from '@/entities/payment'

export default function MarkAsPaidButton({ appointment, size = 'sm', full }) {
  const [open, setOpen] = useState(false)
  const [methodId, setMethodId] = useState(null)
  const methods = usePaymentMethods()
  const mutation = useAppMutation({
    mutationFn: () => markAppointmentPaid(appointment.id, methodId),
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: `Pagamento de ${formatMoney(appointment.valor)} registrado.`,
    onSuccess: () => setOpen(false),
  })

  return (
    <>
      <Button
        variant="secondary"
        size={size}
        full={full}
        onClick={(event) => {
          event.stopPropagation()
          setOpen(true)
        }}
      >
        Marcar como pago
      </Button>
      <BottomSheet
        open={open}
        onClose={() => !mutation.isPending && setOpen(false)}
        title="Forma de pagamento"
        footer={
          <>
            <Button variant="secondary" disabled={mutation.isPending} onClick={() => setOpen(false)}>Cancelar</Button>
            <Button
              loading={mutation.isPending}
              loadingLabel="Salvando..."
              disabled={!methodId}
              onClick={() => mutation.mutate()}
            >
              Confirmar pagamento
            </Button>
          </>
        }
      >
        <div className="flex flex-wrap gap-2">
          {(methods.data ?? []).map((method) => (
            <Chip key={method.id} selected={methodId === method.id} onClick={() => setMethodId(method.id)}>
              {method.titulo.replaceAll('_', ' ')}
            </Chip>
          ))}
        </div>
      </BottomSheet>
    </>
  )
}
