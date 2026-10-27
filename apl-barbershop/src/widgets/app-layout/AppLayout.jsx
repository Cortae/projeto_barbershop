import { useEffect } from 'react'
import { NavLink, useLocation, useNavigate } from 'react-router'
import { ChevronLeft, Plus } from 'lucide-react'
import { Button } from '@/shared/ui'
import { NAV_ITEMS } from './navItems'
import Logo from './Logo'
import AvatarMenu from './AvatarMenu'

/**
 * Estrutura comum: header + nav inferior (mobile) / sidebar + header (desktop).
 * - variant "home": logo + avatar na barra mobile (TL02)
 * - back: mostra botão voltar (telas internas)
 * - menu: ação à direita na barra mobile (ex.: "…" da agenda)
 * - primaryAction: { label, onClick } → FAB no mobile, botão no cabeçalho desktop
 */
export default function AppLayout({ title, variant, back, menu, primaryAction, children }) {
  const { pathname } = useLocation()
  const navigate = useNavigate()
  useEffect(() => {
    document.title = `${title} · JR Barbershop`
  }, [title])
  const goBack = () => (typeof back === 'string' ? navigate(back) : navigate(-1))

  return (
    <div className="min-h-dvh lg:pl-[184px]">
      {/* Sidebar desktop */}
      <aside className="fixed inset-y-0 left-0 hidden w-[184px] flex-col gap-6 border-r border-borda bg-superficie p-4 lg:flex">
        <Logo />
        <nav aria-label="Principal" className="flex flex-col gap-2">
          {NAV_ITEMS.map(({ to, label, icon: Icon, match }) => {
            const active = match(pathname)
            return (
              <NavLink
                key={to}
                to={to}
                aria-current={active ? 'page' : undefined}
                className={`texto-label flex min-h-11 items-center gap-3 rounded-8 px-3 ${
                  active ? 'bg-secundaria font-semibold text-primaria' : 'text-texto-secundario hover:bg-superficie-elevada'
                }`}
              >
                <Icon className="size-6" aria-hidden /> {label}
              </NavLink>
            )
          })}
        </nav>
      </aside>

      {/* Header */}
      <header className="sticky top-0 z-30 flex h-14 items-center gap-2 border-b border-borda bg-superficie px-4 lg:px-8">
        <div className="flex w-11 lg:hidden">
          {variant === 'home' ? null : back && <Button variant="icon" size="icon" icon={ChevronLeft} aria-label="Voltar" onClick={goBack} />}
        </div>
        {variant === 'home' ? (
          <div className="flex-1 lg:hidden"><Logo /></div>
        ) : (
          <h1 className="texto-h3 flex-1 text-center lg:hidden">{title}</h1>
        )}
        <div className="hidden flex-1 items-center gap-3 lg:flex">
          {back && <Button variant="icon" size="icon" icon={ChevronLeft} aria-label="Voltar" onClick={goBack} />}
          <h1 className="texto-h2">{title}</h1>
        </div>
        <div className="flex items-center gap-3">
          {primaryAction && (
            <div className="hidden lg:block">
              <Button size="sm" icon={Plus} onClick={primaryAction.onClick}>{primaryAction.label}</Button>
            </div>
          )}
          {menu}
          <div className={variant === 'home' ? '' : 'hidden lg:block'}><AvatarMenu /></div>
          {!menu && variant !== 'home' && <div className="w-11 lg:hidden" />}
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 pb-40 pt-4 lg:px-8 lg:pb-8 lg:pt-6">{children}</main>

      {primaryAction && (
        <button
          type="button"
          onClick={primaryAction.onClick}
          className="fixed bottom-20 right-4 z-30 flex h-14 items-center gap-2 rounded-full bg-primaria px-5 font-semibold text-fundo hover:bg-primaria-hover active:bg-primaria-pressionado lg:hidden"
        >
          <Plus className="size-5" aria-hidden /> {primaryAction.label}
        </button>
      )}

      {/* Bottom nav mobile */}
      <nav aria-label="Principal" className="fixed inset-x-0 bottom-0 z-30 grid h-16 grid-cols-5 border-t border-borda bg-superficie lg:hidden">
        {NAV_ITEMS.map(({ to, label, icon: Icon, match }) => {
          const active = match(pathname)
          return (
            <NavLink
              key={to}
              to={to}
              aria-current={active ? 'page' : undefined}
              className={`flex flex-col items-center justify-center gap-1 text-[14px] leading-none ${active ? 'font-semibold text-primaria' : 'text-texto-secundario'}`}
            >
              <Icon className="size-6" aria-hidden strokeWidth={active ? 2.5 : 2} />
              <span className="text-[12px] sm:text-[14px]">{label}</span>
            </NavLink>
          )
        })}
      </nav>
    </div>
  )
}
