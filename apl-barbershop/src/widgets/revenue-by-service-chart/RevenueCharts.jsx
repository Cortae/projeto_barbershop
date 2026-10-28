import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  LinearScale,
  Tooltip,
} from 'chart.js'
import { Bar } from 'react-chartjs-2'
import { formatMoney } from '@/shared/lib/format'

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip)

const chartColor = (name, fallback) => {
  if (typeof document === 'undefined') return fallback
  return getComputedStyle(document.documentElement).getPropertyValue(name).trim() || fallback
}

export function ProfitChart({ weeks }) {
  const empty = weeks.every((week) => week.valorBruto === 0 && week.despesas === 0)
  const description = weeks.map((w) => `${w.rotulo}: bruto ${formatMoney(w.valorBruto)}, despesas ${formatMoney(w.despesas)}`).join('; ')
  const data = {
    labels: weeks.map((week) => week.rotulo),
    datasets: [
      {
        label: 'Bruto',
        data: weeks.map((week) => week.valorBruto),
        borderColor: chartColor('--color-primaria', '#22c55e'),
        backgroundColor: chartColor('--color-primaria', '#22c55e'),
        borderRadius: 4,
      },
      {
        label: 'Despesas',
        data: weeks.map((week) => week.despesas),
        borderColor: chartColor('--color-alerta', '#f59e0b'),
        backgroundColor: chartColor('--color-alerta', '#f59e0b'),
        borderRadius: 4,
      },
    ],
  }
  const options = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: { mode: 'index', intersect: false },
    plugins: {
      tooltip: {
        callbacks: { label: ({ dataset, parsed }) => `${dataset.label}: ${formatMoney(parsed.y)}` },
      },
    },
    scales: {
      x: {
        border: { color: chartColor('--color-borda', '#2a2a2a') },
        grid: { display: false },
        ticks: { color: chartColor('--color-texto-secundario', '#a1a1aa') },
      },
      y: {
        beginAtZero: true,
        border: { display: false },
        grid: { color: chartColor('--color-borda', '#2a2a2a') },
        ticks: {
          color: chartColor('--color-texto-secundario', '#a1a1aa'),
          callback: (value) => formatMoney(value),
        },
      },
    },
  }
  return (
    <section aria-labelledby="lucro-mes" className="rounded-12 border border-borda bg-superficie p-4">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <h2 id="lucro-mes" className="texto-h3">Receita no mês</h2>
        <ul className="texto-corpo-pequeno flex gap-4 text-texto-secundario">
          <li className="flex items-center gap-2"><span className="h-0.5 w-4 bg-primaria" /> Bruto</li>
          <li className="flex items-center gap-2"><span className="h-2 w-4 rounded-sm bg-alerta" /> Despesas</li>
        </ul>
      </div>
      <p className="sr-only">{description}</p>
      {empty ? (
        <div className="texto-corpo-pequeno mt-4 flex h-52 items-center justify-center text-texto-secundario">
          Sem movimento neste mês.
        </div>
      ) : (
        <div className="mt-4 h-52">
          <Bar data={data} options={options} role="img" aria-label={description} />
        </div>
      )}
    </section>
  )
}

export function RevenueByService({ items }) {
  return (
    <section aria-labelledby="fat-servico" className="rounded-12 border border-borda bg-superficie p-4">
      <h2 id="fat-servico" className="texto-h3">Faturamento por serviço</h2>
      {items.length === 0 ? (
        <p className="texto-corpo-pequeno mt-3 text-texto-secundario">Nenhum atendimento concluído neste mês.</p>
      ) : (
        <ul className="mt-2 divide-y divide-borda">
          {items.map((s) => (
            <li key={s.servicoId} className="flex items-center gap-3 py-3">
              <span className="flex-1">{s.nome}</span>
              <span className="texto-valor">{formatMoney(s.total)}</span>
              <span className="texto-corpo-pequeno w-10 text-right tabular-nums text-texto-secundario">{s.percentual}%</span>
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
