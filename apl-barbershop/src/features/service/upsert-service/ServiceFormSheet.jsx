import { useState } from 'react'
import { BottomSheet, Button, TextField } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { parseMoney, moneyToInput } from '@/shared/lib/format'
import { saveService } from '@/entities/service'

export default function ServiceFormSheet({ open, service, onClose }) {
  const [form, setForm] = useState({
    nome: service?.nome ?? '',
    valor: service ? moneyToInput(service.valor) : '',
    duracao: service ? String(service.duracaoMinutos) : '',
  })
  const [errors, setErrors] = useState({})
  const mutation = useAppMutation({ mutationFn: saveService, invalidate: ['services'], successMessage: 'Serviço salvo.', onSuccess: onClose })
  const set = (k) => (e) => {
    setForm((f) => ({ ...f, [k]: e.target.value }))
    setErrors((er) => ({ ...er, [k]: undefined }))
  }
  const submit = () => {
    const price = parseMoney(form.valor)
    const durationMin = parseInt(form.duracao, 10)
    const errs = {}
    if (!form.nome.trim()) errs.nome = 'Informe o nome.'
    if (!(price > 0)) errs.valor = 'O preço deve ser maior que zero.'
    if (!(durationMin > 0)) errs.duracao = 'Informe a duração em minutos.'
    setErrors(errs)
    if (!Object.keys(errs).length) mutation.mutate({ id: service?.id, nome: form.nome.trim(), valor: price, duracaoMinutos: durationMin })
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
      <TextField label="Nome do serviço" required placeholder="Ex.: Corte masculino" value={form.nome} onChange={set('nome')} error={errors.nome} disabled={busy} />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Preço (R$)" required inputMode="decimal" placeholder="0,00" value={form.valor} onChange={set('valor')} error={errors.valor} disabled={busy} />
        <TextField label="Duração (min)" required inputMode="numeric" placeholder="30" value={form.duracao} onChange={set('duracao')} error={errors.duracao} disabled={busy} />
      </div>
    </BottomSheet>
  )
}
