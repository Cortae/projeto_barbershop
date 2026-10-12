import { useState } from 'react'
import { BottomSheet, Button, TextField } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { parseCurrency } from '@/shared/lib/format'
import { today } from '@/shared/lib/date'
import { createExpense } from '@/entities/expense'

export default function ExpenseFormSheet({ open, onClose }) {
  const [form, setForm] = useState({ description: '', amount: '', date: today() })
  const [errors, setErrors] = useState({})
  const mutation = useAppMutation({ mutationFn: createExpense, invalidate: ['expenses', 'finance'], successMessage: 'Despesa salva.', onSuccess: onClose })
  const set = (k) => (e) => {
    setForm((f) => ({ ...f, [k]: e.target.value }))
    setErrors((er) => ({ ...er, [k]: undefined }))
  }
  const submit = () => {
    const amountCents = parseCurrency(form.amount)
    const errs = {}
    if (!form.description.trim()) errs.description = 'Informe a descrição.'
    if (!(amountCents > 0)) errs.amount = 'Informe o valor.'
    if (!form.date) errs.date = 'Informe a data.'
    setErrors(errs)
    if (!Object.keys(errs).length) mutation.mutate({ description: form.description.trim(), amountCents, date: form.date })
  }
  const busy = mutation.isPending
  return (
    <BottomSheet
      open={open}
      onClose={() => !busy && onClose()}
      title="Nova despesa"
      footer={
        <>
          <Button variant="secondary" disabled={busy} onClick={onClose}>Cancelar</Button>
          <Button loading={busy} loadingLabel="Salvando..." onClick={submit}>Salvar despesa</Button>
        </>
      }
    >
      <TextField label="Descrição" required placeholder="Ex.: aluguel, produtos" value={form.description} onChange={set('description')} error={errors.description} disabled={busy} />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Valor (R$)" required inputMode="decimal" placeholder="0,00" value={form.amount} onChange={set('amount')} error={errors.amount} disabled={busy} />
        <TextField label="Data" required type="date" value={form.date} onChange={set('date')} error={errors.date} disabled={busy} />
      </div>
      <p className="texto-corpo-pequeno text-texto-secundario">Ao salvar, a lista e o lucro líquido são atualizados.</p>
    </BottomSheet>
  )
}
