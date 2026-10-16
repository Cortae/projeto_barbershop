import { formatMoney } from '@/shared/lib/format'

export function ProfitChart({ weeks }) {
  const max = Math.max(1, ...weeks.map((w) => w.valorBruto))
  const empty = weeks.every((w) => w.valorBruto === 0)
  const description = weeks.map((w) => `${w.rotulo}: bruto ${formatMoney(w.valorBruto)}, líquido ${formatMoney(w.valorLiquido)}`).join('; ')
  return (
    <section aria-labelledby="lucro-mes" className="rounded-12 border border-borda bg-superficie p-4">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <h2 id="lucro-mes" className="texto-h3">Lucro no mês</h2>
        <ul className="texto-corpo-pequeno flex gap-4 text-texto-secundario">
          <li className="flex items-center gap-2"><span className="size-3 rounded-sm bg-primaria" /> Bruto</li>
          <li className="flex items-center gap-2"><span className="hachura-liquido size-3 rounded-sm border border-primaria-hover" /> Líquido</li>
        </ul>
      </div>
      <p className="sr-only">{description}</p>
      <div className="mt-4 flex h-44 items-end gap-4 border-b border-borda" aria-hidden>
        {empty ? (
          <p className="texto-corpo-pequeno m-auto text-texto-secundario">Sem movimento neste mês.</p>
        ) : (
          weeks.map((w) => (
            <div key={w.label} className="flex h-full flex-1 items-end justify-center gap-1">
              <div className="w-1/3 max-w-8 rounded-t-[4px] bg-primaria" style={{ height: `${(w.valorBruto / max) * 100}%` }} />
              <div className="hachura-liquido w-1/3 max-w-8 rounded-t-[4px] border border-primaria-hover" style={{ height: `${(w.valorLiquido / max) * 100}%` }} />
            </div>
          ))
        )}
      </div>
      <div className="mt-2 flex gap-4" aria-hidden>
        {weeks.map((w) => <span key={w.label} className="texto-corpo-pequeno flex-1 text-center text-texto-secundario">{w.label}</span>)}
      </div>
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
