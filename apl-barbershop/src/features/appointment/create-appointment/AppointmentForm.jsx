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
    clientId: initial.clientId ?? '',
    serviceId: initial.serviceId ?? '',
    date: initial.startsAt?.slice(0, 10) ?? initial.date ?? '',
    time: initial.startsAt?.slice(11, 16) ?? initial.time ?? '',
    notes: initial.notes ?? '',
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
  const dayAppts = useAppointments(form.date || '0000-00-00', form.date || '0000-00-00')
  const service = services.data?.find((s) => s.id === form.serviceId)

  const timeOptions = useMemo(() => {
    if (!form.date || !hours.data || !service) return []
    return freeStartTimes(form.date, hours.data, service.durationMin, (startsAt) =>
      !findConflict({ startsAt, durationMin: service.durationMin, ignoreId: initial.id }, dayAppts.data ?? [], blocks.data ?? []),
    ).map((t) => ({ value: t.time, label: t.free ? t.time : `${t.time} · ocupado`, disabled: !t.free }))
  }, [form.date, hours.data, service, dayAppts.data, blocks.data, initial.id])

  const mutation = useAppMutation({
    mutationFn: saveAppointment,
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: (a) => `Agendamento salvo. ${a.client.name}, ${formatDayMonth(a.startsAt)} às ${a.startsAt.slice(11, 16)}.`,
    onSuccess: onSaved,
  })

  const submit = (e) => {
    e.preventDefault()
    const errs = {}
    if (!form.clientId) errs.clientId = 'Selecione o cliente.'
    if (!form.serviceId) errs.serviceId = 'Selecione o serviço.'
    if (!form.date) errs.date = 'Informe a data.'
    if (!form.time) errs.time = 'Selecione o horário.'
    setErrors(errs)
    if (Object.keys(errs).length) return
    mutation.mutate(
      { id: initial.id, clientId: form.clientId, serviceId: form.serviceId, startsAt: `${form.date}T${form.time}`, notes: form.notes, status: initial.status },
      { onError: (err) => err.code === 'conflict' && setConflict(err.message) },
    )
  }

  if (clients.isLoading || services.isLoading) return <SkeletonList rows={5} height="h-12" />
  const busy = mutation.isPending

  return (
    <form onSubmit={submit} noValidate className="flex flex-col gap-4">
      <ClientCombobox clients={clients.data ?? []} value={form.clientId} onChange={set('clientId')} error={errors.clientId} disabled={busy} />
      <Link to={`${ROUTES.clientNew}?retorno=agendamento`} className="-mt-2 inline-flex min-h-11 items-center self-start font-medium text-primaria hover:underline">
        + Novo cliente
      </Link>
      <SelectField
        label="Serviço"
        required
        placeholder="Selecione o serviço"
        value={form.serviceId}
        onChange={(e) => { set('serviceId')(e.target.value); set('time')('') }}
        options={(services.data ?? []).map((s) => ({ value: s.id, label: `${s.name} · ${formatCurrency(s.priceCents)}` }))}
        error={errors.serviceId}
        disabled={busy}
      />
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Data" required type="date" value={form.date} onChange={(e) => { set('date')(e.target.value); set('time')('') }} error={errors.date} disabled={busy} />
        <SelectField
          label="Horário"
          required
          placeholder={service && form.date ? 'Selecione o horário' : 'Escolha serviço e data'}
          value={form.time}
          onChange={(e) => set('time')(e.target.value)}
          options={timeOptions}
          error={errors.time}
          disabled={busy || !timeOptions.length}
        />
      </div>
      {conflict && <Banner>{conflict}</Banner>}
      <TextAreaField label="Observações (opcional)" value={form.notes} onChange={(e) => set('notes')(e.target.value)} disabled={busy} />
      {service && (
        <p className="rounded-8 bg-superficie p-3 texto-corpo-pequeno tabular-nums text-texto-secundario">
          Duração: <span className="text-texto">{service.durationMin} min</span> · Valor: <span className="text-texto">{formatCurrency(service.priceCents)}</span>
        </p>
      )}
      <div className="sticky bottom-16 -mx-4 mt-2 grid grid-cols-2 gap-3 bg-fundo px-4 py-3 lg:static lg:mx-0 lg:flex lg:justify-end lg:bg-transparent lg:p-0">
        <Button variant="secondary" onClick={onCancel} disabled={busy}>Cancelar</Button>
        <Button type="submit" loading={busy} loadingLabel="Salvando...">Salvar agendamento</Button>
      </div>
    </form>
  )
}
