import { useState } from 'react'
import { Trash2 } from 'lucide-react'
import { deleteService } from '@/entities/service'
import { useAppMutation } from '@/shared/lib/mutation'
import { Button, Modal } from '@/shared/ui'

export default function DeleteServiceButton({ service }) {
  const [open, setOpen] = useState(false)
  const mutation = useAppMutation({
    mutationFn: () => deleteService(service.id),
    invalidate: ['services'],
    successMessage: 'Serviço excluído.',
    errorMessage: 'Não foi possível excluir o serviço.',
    onSuccess: () => setOpen(false),
  })

  return (
    <>
      <Button
        variant="icon"
        size="icon"
        icon={Trash2}
        aria-label={`Excluir ${service.nome}`}
        onClick={() => setOpen(true)}
      />
      <Modal
        open={open}
        onClose={() => !mutation.isPending && setOpen(false)}
        title="Excluir este serviço?"
        description={`“${service.nome}” será excluído permanentemente. Serviços presentes em agendamentos não podem ser excluídos.`}
      >
        <Button variant="danger" full loading={mutation.isPending} loadingLabel="Excluindo..." onClick={() => mutation.mutate()}>
          Excluir serviço
        </Button>
        <Button variant="secondary" full data-autofocus disabled={mutation.isPending} onClick={() => setOpen(false)}>Voltar</Button>
      </Modal>
    </>
  )
}
