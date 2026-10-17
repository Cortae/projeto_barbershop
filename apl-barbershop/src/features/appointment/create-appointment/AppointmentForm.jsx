import { useState } from 'react'
import { Link } from 'react-router'
import { Button, TextField, SelectField, Banner, SkeletonList, Chip } from '@/shared/ui'
import { formatMoney, formatDayMonth } from '@/shared/lib/format'
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
    clienteId: initial.clienteId ? Number(initial.clienteId) : '',
    servicos: initial.servicos?.map((servico) => servico.id) ?? [],
    data: initial.horarioInicio?.slice(0, 10) ?? initial.data ?? '',
    hora: initial.horarioInicio?.slice(11, 16) ?? initial.hora ?? '',
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
  const blocks = useBlocks(form.data, form.data)
  const dayAppts = useAppointments(form.data || null, form.data || null)
  const selectedServices = (services.data ?? []).filter((service) => form.servicos.includes(service.id))
  const duration = selectedServices.reduce((total, service) => total + service.duracaoMinutos, 0)
  const value = selectedServices.reduce((total, service) => total + service.valor, 0)

  const timeOptions = (() => {
    if (!form.data || !hours.data || !duration) return []
    return freeStartTimes(form.data, hours.data, duration, (horarioInicio) =>
      !findConflict({ horarioInicio, duracaoMinutos: duration, ignorarId: initial.id }, dayAppts.data ?? [], blocks.data ?? []),
    ).map((t) => ({ value: t.horario, label: t.livre ? t.horario : `${t.horario} · ocupado`, disabled: !t.livre }))
  })()

  const mutation = useAppMutation({
    mutationFn: saveAppointment,
    invalidate: ['appointments', 'clients', 'finance'],
    successMessage: (a) => `Agendamento salvo. ${a.cliente.nome}, ${formatDayMonth(a.horarioInicio)} às ${a.horarioInicio.slice(11, 16)}.`,
    onSuccess: onSaved,
    suppressConflict: true,
  })

  const submit = (e) => {
    e.preventDefault()
    const errs = {}
    if (!form.clienteId) errs.clienteId = 'Selecione o cliente.'
    if (!form.servicos.length) errs.servicos = 'Selecione ao menos um serviço.'
    if (!form.data) errs.data = 'Informe a data.'
    if (!form.hora) errs.hora = 'Selecione o horário.'
    setErrors(errs)
    if (Object.keys(errs).length) return
    mutation.mutate(
      { id: initial.id, barbeiroId: 2, clienteId: form.clienteId, servicos: form.servicos, horarioInicio: `${form.data}T${form.hora}` },
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
      <fieldset>
        <legend className="texto-label mb-2">Serviços *</legend>
        <div className="flex flex-wrap gap-2">
          {(services.data ?? []).map((service) => (
            <Chip
              key={service.id}
              selected={form.servicos.includes(service.id)}
              disabled={busy}
              onClick={() => {
                set('servicos')(
                  form.servicos.includes(service.id)
                    ? form.servicos.filter((id) => id !== service.id)
                    : [...form.servicos, service.id],
                )
                set('hora')('')
              }}
            >
              {service.nome} · {formatMoney(service.valor)}
            </Chip>
          ))}
        </div>
        {errors.servicos && <p className="texto-corpo-pequeno mt-2 text-erro">{errors.servicos}</p>}
      </fieldset>
      <div className="grid grid-cols-2 gap-3">
        <TextField label="Data" required type="date" value={form.data} onChange={(e) => { set('data')(e.target.value); set('hora')('') }} error={errors.data} disabled={busy} />
        <SelectField
          label="Horário"
          required
          placeholder={duration && form.data ? 'Selecione o horário' : 'Escolha serviços e data'}
          value={form.hora}
          onChange={(e) => set('hora')(e.target.value)}
          options={timeOptions}
          error={errors.hora}
          disabled={busy || !timeOptions.length}
        />
      </div>
      {conflict && <Banner>{conflict}</Banner>}
      {selectedServices.length > 0 && (
        <p className="rounded-8 bg-superficie p-3 texto-corpo-pequeno tabular-nums text-texto-secundario">
          Duração: <span className="text-texto">{duration} min</span> · Valor: <span className="text-texto">{formatMoney(value)}</span>
        </p>
      )}
      <div className="sticky bottom-16 -mx-4 mt-2 grid grid-cols-2 gap-3 bg-fundo px-4 py-3 lg:static lg:mx-0 lg:flex lg:justify-end lg:bg-transparent lg:p-0">
        <Button variant="secondary" onClick={onCancel} disabled={busy}>Cancelar</Button>
        <Button type="submit" loading={busy} loadingLabel="Salvando...">Salvar agendamento</Button>
      </div>
    </form>
  )
}
