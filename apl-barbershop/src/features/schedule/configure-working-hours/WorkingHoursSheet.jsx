import { useState } from 'react'
import { BottomSheet, Button, TextField, Chip } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { WEEKDAYS_SHORT } from '@/shared/lib/date'
import { saveWorkingHours } from '@/entities/time-slot'

export default function WorkingHoursSheet({ open, onClose, hours }) {
  const [form, setForm] = useState(hours)
  const mutation = useAppMutation({
    mutationFn: saveWorkingHours,
    invalidate: ['schedule'],
    successMessage: 'Expediente salvo.',
    onSuccess: onClose,
  })
  const set = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }))
  const toggleDay = (d) => setForm((f) => ({ ...f, days: f.days.includes(d) ? f.days.filter((x) => x !== d) : [...f.days, d].sort() }))
  const busy = mutation.isPending
  return (
    <BottomSheet
      open={open}
      onClose={() => !busy && onClose()}
      title="Expediente"
      footer={
        <>
          <Button variant="secondary" disabled={busy} onClick={onClose}>Cancelar</Button>
          <Button loading={busy} loadingLabel="Salvando..." onClick={() => mutation.mutate(form)}>Salvar expediente</Button>
        </>
      }
    >
      <fieldset>
        <legend className="texto-label mb-2">Dias de funcionamento</legend>
        <div className="flex flex-wrap gap-2">
          {WEEKDAYS_SHORT.map((label, d) => (
            <Chip key={label} selected={form.days.includes(d)} onClick={() => toggleDay(d)} disabled={busy}>{label}</Chip>
          ))}
        </div>
      </fieldset>
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Abertura" type="time" value={form.opensAt} onChange={set('opensAt')} disabled={busy} />
        <TextField label="Fechamento" type="time" value={form.closesAt} onChange={set('closesAt')} disabled={busy} />
      </div>
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Intervalo (opcional) · início" type="time" value={form.breakStart ?? ''} onChange={set('breakStart')} disabled={busy} />
        <TextField label="Intervalo · fim" type="time" value={form.breakEnd ?? ''} onChange={set('breakEnd')} disabled={busy} />
      </div>
    </BottomSheet>
  )
}
