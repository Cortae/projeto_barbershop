export default function Tabs({ tabs, value, onChange, label }) {
  const onKey = (e) => {
    const i = tabs.findIndex((t) => t.value === value)
    if (e.key === 'ArrowRight') onChange(tabs[(i + 1) % tabs.length].value)
    if (e.key === 'ArrowLeft') onChange(tabs[(i - 1 + tabs.length) % tabs.length].value)
  }
  return (
    <div role="tablist" aria-label={label} className="grid border-b border-borda" style={{ gridTemplateColumns: `repeat(${tabs.length}, 1fr)` }}>
      {tabs.map((t) => {
        const active = t.value === value
        return (
          <button
            key={t.value}
            role="tab"
            aria-selected={active}
            tabIndex={active ? 0 : -1}
            onClick={() => onChange(t.value)}
            onKeyDown={onKey}
            className={`-mb-px h-11 border-b-2 ${active ? 'border-primaria font-semibold text-texto' : 'border-transparent text-texto-secundario'}`}
          >
            {t.label}
          </button>
        )
      })}
    </div>
  )
}
