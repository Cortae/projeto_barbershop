import { useMemo, useState } from 'react'
import { Link } from 'react-router'
import { Button, SelectField, TextField, TextAreaField, Banner, SkeletonList } from '@/shared/ui'
import { formatCurrency, formatDayMonth } from '@/shared/lib/format'
import { useAppMutation } from '@/shared/lib/mutation'
import { ROUTES } from '@/shared/config'
import { useClients } from '@/entities/client'
import { useServices } from '@/entities/service'
import { useAppointments, saveAppointment, findConflict } from '@/entities/appointment'
import { useBlocks, useWorkingHours, freeStartTimes } from '@/entities/time-slot'
import ClientCombobox from './ClientCombobox'

/** Formulário TL04: cria ou remarca (initial.id) um atendimento. */
export default function AppointmentForm({ initial = {}, onSaved, onCancel }) {
  const [form, setForm] = useState({
    clienteId: initial.clienteId ?? '',
    servicoId: initial.servicoId ?? '',
    data: initial.horarioInicio?.slice(0, 10) ?? initial.data ?? '',
    hora: initial.horarioInicio?.slice(11, 16) ?? initial.hora ?? '',
    observacoes: initial.observacoes ?? '',
  })
  const [errors, setErrors] = useState({})
  const [conflict, setConflict] = useState(null)
  const set = (k) => (v) => {
    setForm((f) => ({ ...f, [k]: v }))
    setErrors((e) => ({ ...e, [k]: undefined }))
    setConflict(null)
  }

  const clients = useClients()
  const services = useServices()
  const hours = useWorkingHours()
  const blocks = useBlocks()
  const dayAppts = useAppointments(form.data || '0000-00-00', form.data || '0000-00-00')
  const service = services.data?.find((s) => s.id === form.servicoId)

  const timeOptions = useMemo(() => {
    if (!form.data || !hours.data || !service) return []
    return freeStartTimes(form.data, hours.data, service.duracaoMinutos, (horarioInicio) =>
      !findConflict({ horarioInicio, duracaoMinutos: service.duracaoMinutos, ignorarId: initial.id }, dayAppts.data ?? [], blocks.data ?? []),
    ).map((t) => ({ value: t.horario, label: t.livre ? t.horario : `${t.horario} · ocupado`, disabled: !t.livre }))
  }, [form.data, hours.data, service, dayAppts.data, blocks.data, initial.id])

  const mutation = useAppMutation({
    mutationFn: saveAppointment,
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: (a) => `Agendamento salvo. ${a.cliente.nome}, ${formatDayMonth(a.horarioInicio)} às ${a.horarioInicio.slice(11, 16)}.`,
    onSuccess: onSaved,
  })

  const submit = (e) => {
    e.preventDefault()
    const errs = {}
    if (!form.clienteId) errs.clienteId = 'Selecione o cliente.'
    if (!form.servicoId) errs.servicoId = 'Selecione o serviço.'
    if (!form.data) errs.data = 'Informe a data.'
    if (!form.hora) errs.hora = 'Selecione o horário.'
    setErrors(errs)
    if (Object.keys(errs).length) return
    mutation.mutate(
      { id: initial.id, clienteId: form.clienteId, servicoId: form.servicoId, horarioInicio: `${form.data}T${form.hora}`, observacoes: form.observacoes, status: initial.status },
      { onError: (err) => err.code === 'conflict' && setConflict(err.message) },
    )
  }

  if (clients.isLoading || services.isLoading) return <SkeletonList rows={5} height="h-12" />
  const busy = mutation.isPending

  return (
    <form onSubmit={submit} noValidate className="flex flex-col gap-4">
      <ClientCombobox clients={clients.data ?? []} value={form.clienteId} onChange={set('clienteId')} error={errors.clienteId} disabled={busy} />
      <Link to={`${ROUTES.clientNew}?retorno=agendamento`} className="-mt-2 inline-flex min-h-11 items-center self-start font-medium text-primaria hover:underline">
        + Novo cliente
      </Link>
      <SelectField
        label="Serviço"
        required
        placeholder="Selecione o serviço"
        value={form.servicoId}
        onChange={(e) => { set('servicoId')(e.target.value); set('hora')('') }}
        options={(services.data ?? []).map((s) => ({ value: s.id, label: `${s.nome} · ${formatCurrency(s.valorCentavos)}` }))}
        error={errors.servicoId}
        disabled={busy}
      />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Data" required type="date" value={form.data} onChange={(e) => { set('data')(e.target.value); set('hora')('') }} error={errors.data} disabled={busy} />
        <SelectField
          label="Horário"
          required
          placeholder={service && form.data ? 'Selecione o horário' : 'Escolha serviço e data'}
          value={form.hora}
          onChange={(e) => set('hora')(e.target.value)}
          options={timeOptions}
          error={errors.hora}
          disabled={busy || !timeOptions.length}
        />
      </div>
      {conflict && <Banner>{conflict}</Banner>}
      <TextAreaField label="Observações (opcional)" value={form.observacoes} onChange={(e) => set('observacoes')(e.target.value)} disabled={busy} />
      {service && (
        <p className="rounded-8 bg-superficie p-3 texto-corpo-pequeno tabular-nums text-texto-secundario">
          Duração: <span className="text-texto">{service.duracaoMinutos} min</span> · Valor: <span className="text-texto">{formatCurrency(service.valorCentavos)}</span>
        </p>
      )}
      <div className="sticky bottom-16 -mx-4 mt-2 grid grid-cols-2 gap-3 bg-fundo px-4 py-3 lg:static lg:mx-0 lg:flex lg:justify-end lg:bg-transparent lg:p-0">
        <Button variant="secondary" onClick={onCancel} disabled={busy}>Cancelar</Button>
        <Button type="submit" loading={busy} loadingLabel="Salvando...">Salvar agendamento</Button>
      </div>
    </form>
  )
}
