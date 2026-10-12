import { useState } from 'react'
import { Button, Modal } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { cancelAppointment } from '@/entities/appointment'

export default function CancelAppointmentButton({ appointmentId, disabled, onDone }) {
  const [open, setOpen] = useState(false)
  const mutation = useAppMutation({
    mutationFn: () => cancelAppointment(appointmentId),
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: 'Agendamento cancelado. O horário ficou livre.',
    onSuccess: onDone,
  })
  return (
    <>
      <Button variant="danger-outline" full disabled={disabled} onClick={() => setOpen(true)}>Cancelar agendamento</Button>
      <Modal open={open} onClose={() => !mutation.isPending && setOpen(false)} title="Cancelar este agendamento?" description="O horário ficará livre na agenda.">
        <Button variant="danger" full loading={mutation.isPending} loadingLabel="Cancelando..." onClick={() => mutation.mutate()}>
          Cancelar agendamento
        </Button>
        <Button variant="secondary" full data-autofocus disabled={mutation.isPending} onClick={() => setOpen(false)}>Voltar</Button>
      </Modal>
    </>
  )
}
