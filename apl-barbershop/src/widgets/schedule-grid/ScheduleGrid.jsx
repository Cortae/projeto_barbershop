import { useNavigate } from 'react-router'
import { Plus, Ban, CalendarX } from 'lucide-react'
import { EmptyState, Button } from '@/shared/ui'
import { ROUTES } from '@/shared/config'
import { formatTime } from '@/shared/lib/format'
import { weekDays, weekdayShort, today } from '@/shared/lib/date'
import { buildDaySlots } from '@/entities/time-slot'

const ROW = 56 // px por bloco de 30 min

export function Legend() {
  return (
    <ul className="texto-corpo-pequeno flex flex-wrap gap-4 text-texto-secundario" aria-label="Legenda">
      <li className="flex items-center gap-2"><span className="size-4 rounded border border-dashed border-borda-campo" /> Livre</li>
      <li className="flex items-center gap-2"><span className="size-4 rounded border border-borda-campo bg-superficie-elevada" /> Ocupado</li>
      <li className="flex items-center gap-2"><span className="hachura-info size-4 rounded border border-info" /> Bloqueado</li>
    </ul>
  )
}

export function DayGrid({ date, hours, appointments, blocks, onBlock }) {
  const navigate = useNavigate()
  const slots = buildDaySlots(date, hours, appointments, blocks)
  if (!slots.length)
    return (
      <EmptyState icon={CalendarX} title="Fora do expediente" text="A barbearia não abre neste dia. Ajuste o expediente pelo menu ⋯ se precisar." />
    )
  const hasAppts = slots.some((s) => s.estado === 'ocupado')
  return (
    <div className="flex flex-col gap-4">
      {!hasAppts && (
        <div className="flex flex-col gap-3 rounded-12 border border-borda bg-superficie p-4">
          <p className="texto-h3">Nenhum atendimento neste dia</p>
          <p className="texto-corpo-pequeno text-texto-secundario">Todos os horários do expediente estão livres.</p>
          <div className="flex flex-wrap gap-3">
            <Button size="sm" onClick={() => navigate(`${ROUTES.appointmentNew}?data=${date}`)}>+ Novo agendamento</Button>
            <Button size="sm" variant="secondary" onClick={onBlock}>Bloquear horário</Button>
          </div>
        </div>
      )}
      <ol className="flex flex-col gap-2">
        {slots.map((s) => (
          <li key={s.horario} className="flex gap-3" style={{ minHeight: s.intervalos * ROW - 8 }}>
            <span className="texto-corpo-pequeno w-12 shrink-0 pt-3 tabular-nums text-texto-secundario">{s.horario}</span>
            {s.estado === 'livre' && (
              <button
                onClick={() => navigate(`${ROUTES.appointmentNew}?data=${date}&hora=${s.horario}`)}
                aria-label={`Livre às ${s.horario}. Novo agendamento`}
                className="flex flex-1 items-center gap-2 rounded-8 border border-dashed border-borda-campo px-3 text-texto-secundario hover:border-primaria hover:text-primaria"
              >
                <Plus className="size-5" aria-hidden /> Livre
              </button>
            )}
            {s.estado === 'ocupado' && (
              <button
                onClick={() => navigate(ROUTES.appointment(s.agendamento.id))}
                className={`flex flex-1 flex-col justify-center rounded-8 border px-3 py-2 text-left hover:border-primaria ${
                  s.agendamento.status === 'concluido' ? 'border-borda bg-superficie' : 'border-borda-campo bg-superficie-elevada'
                }`}
              >
                <span className="font-medium">{s.agendamento.cliente.nome} · {s.agendamento.servicos.map((servico) => servico.nome).join(', ')} · {s.agendamento.duracaoMinutos} min</span>
                <span className="texto-corpo-pequeno text-texto-secundario">
                  {s.agendamento.status === 'concluido' ? 'Concluído' : 'Agendado'}
                  {s.agendamento.statusPagamento === 'pendente' ? ' · Pendente' : ''}
                </span>
              </button>
            )}
            {s.estado === 'bloqueado' && (
              <div className="hachura-info flex flex-1 items-center gap-2 rounded-8 border border-info px-3">
                <Ban className="size-5 text-info" aria-hidden />
                <span>Bloqueado{s.motivo ? ` · ${s.motivo}` : ''}</span>
              </div>
            )}
          </li>
        ))}
      </ol>
    </div>
  )
}

export function WeekGrid({ date, appointments, onSelectDay }) {
  const navigate = useNavigate()
  const t = today()
  return (
    <div className="-mx-4 overflow-x-auto px-4">
      <div className="grid min-w-[700px] grid-cols-7 gap-2">
        {weekDays(date).map((d) => {
          const list = appointments.filter((a) => a.horarioInicio.startsWith(d))
          const isToday = d === t
          return (
            <div key={d} className="flex flex-col gap-2">
              <button
                onClick={() => onSelectDay(d)}
                aria-label={`Ver ${weekdayShort(d)} ${d.slice(8)}`}
                className={`flex min-h-11 flex-col items-center justify-center rounded-8 py-1 ${isToday ? 'bg-primaria text-fundo' : 'bg-superficie text-texto hover:bg-superficie-elevada'}`}
              >
                <span className="texto-corpo-pequeno">{weekdayShort(d)}</span>
                <span className="font-semibold tabular-nums">{d.slice(8)}</span>
              </button>
              {list.length === 0 && <span className="texto-corpo-pequeno py-2 text-center text-texto-secundario">—</span>}
              {list.map((a) => (
                <button
                  key={a.id}
                  onClick={() => navigate(ROUTES.appointment(a.id))}
                  className="rounded-8 border border-borda-campo bg-superficie-elevada p-2 text-left hover:border-primaria"
                >
                  <span className="texto-label block tabular-nums text-texto-secundario">{formatTime(a.horarioInicio)}</span>
                  <span className="texto-corpo-pequeno block truncate">{a.cliente.nome}</span>
                </button>
              ))}
            </div>
          )
        })}
      </div>
    </div>
  )
}
