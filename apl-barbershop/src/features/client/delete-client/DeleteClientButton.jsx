import { useState } from 'react'
import { Trash2 } from 'lucide-react'
import { deleteClient } from '@/entities/client'
import { useAppMutation } from '@/shared/lib/mutation'
import { Button, Modal } from '@/shared/ui'

export default function DeleteClientButton({ client, onDeleted }) {
  const [open, setOpen] = useState(false)
  const mutation = useAppMutation({
    mutationFn: () => deleteClient(client.id),
    invalidate: ['clients'],
    successMessage: 'Cliente excluído.',
    errorMessage: 'Não foi possível excluir o cliente.',
    onSuccess: onDeleted,
  })

  return (
    <>
      <Button variant="danger-outline" icon={Trash2} full onClick={() => setOpen(true)}>Excluir cliente</Button>
      <Modal
        open={open}
        onClose={() => !mutation.isPending && setOpen(false)}
        title="Excluir este cliente?"
        description={`“${client.nome}” será excluído permanentemente. Clientes com agendamentos não podem ser excluídos.`}
      >
        <Button variant="danger" full loading={mutation.isPending} loadingLabel="Excluindo..." onClick={() => mutation.mutate()}>
          Excluir cliente
        </Button>
        <Button variant="secondary" full data-autofocus disabled={mutation.isPending} onClick={() => setOpen(false)}>Voltar</Button>
      </Modal>
    </>
  )
}
