import { useState } from 'react'
import { BottomSheet, Button, TextField, Banner } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { createBlock } from '@/entities/time-slot'

export default function BlockTimeSlotSheet({ open, onClose, date }) {
  const [form, setForm] = useState({ date, start: '12:00', end: '13:00', reason: '' })
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
    if (!form.date || !form.start || !form.end) return setError('Informe data, início e fim.')
    if (form.end <= form.start) return setError('O fim deve ser depois do início.')
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
      <TextField label="Data" type="date" value={form.date} onChange={set('date')} disabled={busy} />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Início" type="time" value={form.start} onChange={set('start')} disabled={busy} />
        <TextField label="Fim" type="time" value={form.end} onChange={set('end')} disabled={busy} />
      </div>
      <TextField label="Motivo (opcional)" placeholder="Ex.: almoço, compromisso" value={form.reason} onChange={set('reason')} disabled={busy} />
      {error && <Banner>{error}</Banner>}
    </BottomSheet>
  )
}
