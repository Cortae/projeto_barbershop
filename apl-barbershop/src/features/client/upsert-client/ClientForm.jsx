import { useState } from 'react'
import { Button, TextField, TextAreaField } from '@/shared/ui'
import { useAppMutation } from '@/shared/lib/mutation'
import { saveClient } from '@/entities/client'

const maskPhone = (v) => {
  const d = v.replace(/\D/g, '').slice(0, 11)
  if (d.length <= 2) return d
  if (d.length <= 7) return `(${d.slice(0, 2)}) ${d.slice(2)}`
  return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7)}`
}

export default function ClientForm({ client, onSaved }) {
  const [form, setForm] = useState({ nome: '', telefone: '', email: '', dataNascimento: '', observacoes: '', ...client })
  const [errors, setErrors] = useState({})
  const mutation = useAppMutation({ mutationFn: saveClient, invalidate: ['clients', 'appointments'], successMessage: 'Cliente salvo.', onSuccess: onSaved })
  const set = (k, fn = (x) => x) => (e) => {
    setForm((f) => ({ ...f, [k]: fn(e.target.value) }))
    setErrors((er) => ({ ...er, [k]: undefined }))
  }
  const submit = (e) => {
    e.preventDefault()
    const errs = {}
    if (!form.nome.trim()) errs.nome = 'Informe o nome.'
    if (form.telefone.replace(/\D/g, '').length < 10) errs.telefone = 'Informe o telefone.'
    setErrors(errs)
    if (!Object.keys(errs).length) mutation.mutate({ id: form.id, nome: form.nome.trim(), telefone: form.telefone, email: form.email, dataNascimento: form.dataNascimento, observacoes: form.observacoes })
  }
  const busy = mutation.isPending
  return (
    <form onSubmit={submit} noValidate className="flex flex-col gap-4">
      <TextField label="Nome" required placeholder="Nome do cliente" autoComplete="name" value={form.nome} onChange={set('nome')} error={errors.nome} disabled={busy} />
      <TextField label="Telefone" required type="tel" inputMode="tel" placeholder="(11) 90000-0000" value={form.telefone} onChange={set('telefone', maskPhone)} error={errors.telefone} disabled={busy} />
      <TextField label="E-mail (opcional)" type="email" placeholder="nome@email.com" value={form.email} onChange={set('email')} disabled={busy} />
      <TextField label="Data de nascimento (opcional)" type="date" value={form.dataNascimento} onChange={set('dataNascimento')} disabled={busy} />
      <TextAreaField label="Observações (opcional)" value={form.observacoes} onChange={set('observacoes')} disabled={busy} />
      <Button type="submit" size="lg" full loading={busy} loadingLabel="Salvando..." className="lg:h-12 lg:w-auto lg:self-end">Salvar cliente</Button>
    </form>
  )
}
