import { useState } from 'react'
import { Button, BottomSheet, Chip, Segmented } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { completeAppointment } from '@/entities/appointment'
import { usePaymentMethods } from '@/entities/payment'

export default function CompleteAppointmentButton({ appointmentId, onDone }) {
  const [open, setOpen] = useState(false)
  const [status, setStatus] = useState('pago')
  const [methodId, setMethodId] = useState(null)
  const methods = usePaymentMethods()
  const mutation = useAppMutation({
    mutationFn: () => completeAppointment(appointmentId, status === 'pago' ? methodId : null),
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: status === 'pago' ? 'Atendimento concluído e pago.' : 'Atendimento concluído. Pagamento pendente.',
    onSuccess: onDone,
  })
  const busy = mutation.isPending
  const submit = () => {
    if (status === 'pago' && !methodId) return
    mutation.mutate()
  }
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
            <Button loading={busy} loadingLabel="Concluindo..." disabled={status === 'pago' && !methodId} onClick={submit}>Concluir</Button>
          </>
        }
      >
        <Segmented label="Pagamento" value={status} onChange={setStatus} options={[{ value: 'pago', label: 'Pago' }, { value: 'pendente', label: 'Pendente' }]} />
        {status === 'pago' && (
          <fieldset>
            <legend className="texto-label mb-2">Forma de pagamento</legend>
            <div className="flex flex-wrap gap-2">
              {(methods.data ?? []).map((m) => (
                <Chip key={m.id} selected={methodId === m.id} onClick={() => setMethodId(m.id)} disabled={busy}>{m.titulo.replaceAll('_', ' ')}</Chip>
              ))}
            </div>
          </fieldset>
        )}
      </BottomSheet>
    </>
  )
}
