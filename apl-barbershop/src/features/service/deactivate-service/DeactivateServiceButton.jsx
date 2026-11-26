import { useState } from 'react'
import { Trash2 } from 'lucide-react'
import { deactivateService } from '@/entities/service'
import { useAppMutation } from '@/shared/lib/mutation'
import { Button, Modal } from '@/shared/ui'

export default function DeactivateServiceButton({ service }) {
  const [open, setOpen] = useState(false)
  const mutation = useAppMutation({
    mutationFn: () => deactivateService(service.id),
    invalidate: ['services'],
    successMessage: 'Serviço desativado.',
    errorMessage: 'Não foi possível desativar o serviço.',
    onSuccess: () => setOpen(false),
  })

  return (
    <>
      <Button
        variant="icon"
        size="icon"
        icon={Trash2}
        aria-label={`Desativar ${service.nome}`}
        onClick={() => setOpen(true)}
      />
      <Modal
        open={open}
        onClose={() => !mutation.isPending && setOpen(false)}
        title="Desativar este serviço?"
        description={`“${service.nome}” deixará de aparecer nos serviços disponíveis e em novos agendamentos. O histórico será preservado.`}
      >
        <Button variant="danger" full loading={mutation.isPending} loadingLabel="Desativando..." onClick={() => mutation.mutate()}>
          Desativar serviço
        </Button>
        <Button variant="secondary" full data-autofocus disabled={mutation.isPending} onClick={() => setOpen(false)}>Voltar</Button>
      </Modal>
    </>
  )
}
