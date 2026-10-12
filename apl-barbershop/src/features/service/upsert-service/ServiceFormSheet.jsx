import { useState } from 'react'
import { BottomSheet, Button, TextField } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { parseCurrency, centsToInput } from '@/shared/lib/format'
import { saveService } from '@/entities/service'

export default function ServiceFormSheet({ open, service, onClose }) {
  const [form, setForm] = useState({
    name: service?.name ?? '',
    price: service ? centsToInput(service.priceCents) : '',
    duration: service ? String(service.durationMin) : '',
  })
  const [errors, setErrors] = useState({})
  const mutation = useAppMutation({ mutationFn: saveService, invalidate: ['services'], successMessage: 'Serviço salvo.', onSuccess: onClose })
  const set = (k) => (e) => {
    setForm((f) => ({ ...f, [k]: e.target.value }))
    setErrors((er) => ({ ...er, [k]: undefined }))
  }
  const submit = () => {
    const priceCents = parseCurrency(form.price)
    const durationMin = parseInt(form.duration, 10)
    const errs = {}
    if (!form.name.trim()) errs.name = 'Informe o nome.'
    if (!(priceCents > 0)) errs.price = 'O preço deve ser maior que zero.'
    if (!(durationMin > 0)) errs.duration = 'Informe a duração em minutos.'
    setErrors(errs)
    if (!Object.keys(errs).length) mutation.mutate({ id: service?.id, name: form.name.trim(), priceCents, durationMin })
  }
  const busy = mutation.isPending
  return (
    <BottomSheet
      open={open}
      onClose={() => !busy && onClose()}
      title={service ? 'Editar serviço' : 'Novo serviço'}
      footer={
        <>
          <Button variant="secondary" disabled={busy} onClick={onClose}>Cancelar</Button>
          <Button loading={busy} loadingLabel="Salvando..." onClick={submit}>Salvar serviço</Button>
        </>
      }
    >
      <TextField label="Nome do serviço" required placeholder="Ex.: Corte masculino" value={form.name} onChange={set('name')} error={errors.name} disabled={busy} />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Preço (R$)" required inputMode="decimal" placeholder="0,00" value={form.price} onChange={set('price')} error={errors.price} disabled={busy} />
        <TextField label="Duração (min)" required inputMode="numeric" placeholder="30" value={form.duration} onChange={set('duration')} error={errors.duration} disabled={busy} />
      </div>
    </BottomSheet>
  )
}
