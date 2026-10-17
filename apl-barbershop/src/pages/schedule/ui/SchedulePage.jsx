import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router'
import { MoreHorizontal, Ban } from 'lucide-react'
import { AppLayout } from '@/widgets/app-layout'
import { DayGrid, WeekGrid, Legend } from '@/widgets/schedule-grid'
import { useScheduleParams } from '@/features/schedule/switch-period-view'
import { BlockTimeSlotSheet } from '@/features/schedule/block-time-slot'
import { useAppointments } from '@/entities/appointment'
import { useBlocks, useWorkingHours } from '@/entities/time-slot'
import { Button, Tabs, PeriodSelector, SkeletonList, ErrorState } from '@/shared/ui'
import { addDays, weekDays, weekdayLong, today } from '@/shared/lib/date'
import { formatDayMonth } from '@/shared/lib/format'
import { ROUTES } from '@/shared/config'

function ScheduleMenu({ onBlock }) {
  const [open, setOpen] = useState(false)
  const ref = useRef(null)
  useEffect(() => {
    if (!open) return
    const close = (e) => !ref.current?.contains(e.target) && setOpen(false)
    document.addEventListener('mousedown', close)
    return () => document.removeEventListener('mousedown', close)
  }, [open])
  const item = (label, Icon, fn) => (
    <button role="menuitem" onClick={() => { setOpen(false); fn() }} className="flex min-h-11 w-full items-center gap-2 rounded-8 px-3 hover:bg-superficie">
      <Icon className="size-5" aria-hidden /> {label}
    </button>
  )
  return (
    <div ref={ref} className="relative">
      <Button variant="icon" size="icon" icon={MoreHorizontal} aria-label="Mais opções da agenda" aria-haspopup="menu" aria-expanded={open} onClick={() => setOpen((o) => !o)} />
      {open && (
        <div role="menu" className="absolute right-0 top-full z-40 mt-2 w-52 rounded-12 border border-borda bg-superficie-elevada p-2">
          {item('Bloquear horário', Ban, onBlock)}
        </div>
      )}
    </div>
  )
}

export default function SchedulePage() {
  const navigate = useNavigate()
  const { vista: view, data: date, definirVista: setView, definirData: setDate, irParaDia: goToDay } = useScheduleParams()
  const [sheet, setSheet] = useState(null)
  const days = weekDays(date)
  const [from, to] = view === 'dia' ? [date, date] : [days[0], days[6]]
  const appts = useAppointments(from, to)
  const blocks = useBlocks(from, to)
  const hours = useWorkingHours()
  const loading = appts.isLoading || blocks.isLoading || hours.isLoading
  const error = appts.isError || blocks.isError || hours.isError
  const step = view === 'dia' ? 1 : 7
  const label = view === 'dia' ? `${weekdayLong(date)}, ${formatDayMonth(date)}` : `${formatDayMonth(days[0])} a ${formatDayMonth(days[6])}`

  return (
    <AppLayout
      title="Agenda"
      back={ROUTES.home}
      menu={<ScheduleMenu onBlock={() => setSheet('block')} />}
      primaryAction={{ label: 'Novo agendamento', onClick: () => navigate(`${ROUTES.appointmentNew}?data=${date}`) }}
    >
      <div className="flex flex-col gap-4">
        <Tabs label="Visão da agenda" value={view} onChange={setView} tabs={[{ value: 'dia', label: 'Dia' }, { value: 'semana', label: 'Semana' }]} />
        <div className="flex items-center gap-3">
          <div className="flex-1">
            <PeriodSelector label={label} disabled={loading} onPrev={() => setDate(addDays(date, -step))} onNext={() => setDate(addDays(date, step))} prevLabel={view === 'dia' ? 'Dia anterior' : 'Semana anterior'} nextLabel={view === 'dia' ? 'Próximo dia' : 'Próxima semana'} />
          </div>
          <Button variant="secondary" size="sm" disabled={date === today()} onClick={() => setDate(today())}>Hoje</Button>
        </div>
        <Legend />
        {loading && <SkeletonList rows={6} height="h-12" />}
        {error && <ErrorState title="Não foi possível carregar a agenda" onRetry={() => { appts.refetch(); blocks.refetch(); hours.refetch() }} />}
        {!loading && !error && (view === 'dia' ? (
          <DayGrid date={date} hours={hours.data} appointments={appts.data} blocks={blocks.data} onBlock={() => setSheet('block')} />
        ) : (
          <WeekGrid date={date} appointments={appts.data} onSelectDay={goToDay} />
        ))}
      </div>
      {sheet === 'block' && <BlockTimeSlotSheet open date={date} onClose={() => setSheet(null)} />}
    </AppLayout>
  )
}
