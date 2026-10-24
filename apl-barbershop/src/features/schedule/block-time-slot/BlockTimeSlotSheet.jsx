import { useState } from 'react'
import { BottomSheet, Button, TextField, Banner } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { createBlock } from '@/entities/time-slot'

export default function BlockTimeSlotSheet({ open, onClose, date }) {
  const [form, setForm] = useState({ data: date, inicio: '12:00', fim: '13:00', motivo: '' })
  const [error, setError] = useState(null)
  const mutation = useAppMutation({
    mutationFn: createBlock,
    invalidate: ['schedule', 'appointments'],
    successMessage: 'Horário bloqueado.',
    errorMessage: 'Não foi possível salvar o bloqueio. Tente novamente.',
    onSuccess: onClose,
  })
  const set = (k) => (e) => {
    setForm((f) => ({ ...f, [k]: e.target.value }))
    setError(null)
  }
  const submit = () => {
    if (!form.data || !form.inicio || !form.fim) return setError('Informe data, início e fim.')
    if (form.fim <= form.inicio) return setError('O fim deve ser depois do início.')
    mutation.mutate(form, { onError: (err) => err.code === 'conflict' && setError(err.message) })
  }
  const busy = mutation.isPending
  return (
    <BottomSheet
      open={open}
      onClose={() => !busy && onClose()}
      title="Bloquear horário"
      footer={
        <>
          <Button variant="secondary" disabled={busy} onClick={onClose}>Cancelar</Button>
          <Button loading={busy} loadingLabel="Salvando..." onClick={submit}>Bloquear</Button>
        </>
      }
    >
      <TextField label="Data" type="date" value={form.data} onChange={set('data')} disabled={busy} />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Início" type="time" value={form.inicio} onChange={set('inicio')} disabled={busy} />
        <TextField label="Fim" type="time" value={form.fim} onChange={set('fim')} disabled={busy} />
      </div>
      <TextField label="Motivo (opcional)" placeholder="Ex.: almoço, compromisso" value={form.motivo} onChange={set('motivo')} disabled={busy} />
      {error && <Banner>{error}</Banner>}
    </BottomSheet>
  )
}
