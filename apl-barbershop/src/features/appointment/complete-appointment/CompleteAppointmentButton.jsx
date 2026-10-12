import { useState } from 'react'
import { Button, BottomSheet, Chip, Segmented } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { completeAppointment } from '@/entities/appointment'
import { PAYMENT_METHODS } from '@/entities/payment'

export default function CompleteAppointmentButton({ appointmentId, onDone }) {
  const [open, setOpen] = useState(false)
  const [status, setStatus] = useState('pago')
  const [method, setMethod] = useState('pix')
  const mutation = useAppMutation({
    mutationFn: () => completeAppointment(appointmentId, { paymentStatus: status, paymentMethod: method }),
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: status === 'pago' ? 'Atendimento concluído e pago.' : 'Atendimento concluído. Pagamento pendente.',
    onSuccess: onDone,
  })
  const busy = mutation.isPending
  return (
    <>
      <Button size="lg" full onClick={() => setOpen(true)}>Concluir atendimento</Button>
      <BottomSheet
        open={open}
        onClose={() => !busy && setOpen(false)}
        title="Como foi o pagamento?"
        footer={
          <>
            <Button variant="secondary" disabled={busy} onClick={() => setOpen(false)}>Cancelar</Button>
            <Button loading={busy} loadingLabel="Concluindo..." onClick={() => mutation.mutate()}>Concluir</Button>
          </>
        }
      >
        <Segmented label="Pagamento" value={status} onChange={setStatus} options={[{ value: 'pago', label: 'Pago' }, { value: 'pendente', label: 'Pendente' }]} />
        {status === 'pago' && (
          <fieldset>
            <legend className="texto-label mb-2">Forma de pagamento</legend>
            <div className="flex flex-wrap gap-2">
              {PAYMENT_METHODS.map((m) => (
                <Chip key={m.value} selected={method === m.value} onClick={() => setMethod(m.value)} disabled={busy}>{m.label}</Chip>
              ))}
            </div>
          </fieldset>
        )}
      </BottomSheet>
    </>
  )
}
