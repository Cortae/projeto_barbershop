import { useId } from 'react'
import { useDialog } from '../modal/useDialog'

/** Mobile: ancorado na base. Desktop: painel centralizado. */
export default function BottomSheet({ open, onClose, title, children, footer }) {
  const ref = useDialog(open, onClose)
  const titleId = useId()
  if (!open) return null
  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center lg:items-center">
      <button
        type="button"
        aria-label="Fechar painel"
        className="absolute inset-0 bg-fundo/60"
        onClick={onClose}
      />
      <div
        ref={ref}
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        className="relative z-10 max-h-[90vh] w-full overflow-y-auto rounded-t-20 bg-superficie-elevada p-4 pb-6 lg:max-w-lg lg:rounded-12 lg:border lg:border-borda"
      >
        <div className="mx-auto mb-4 h-1 w-10 rounded-full bg-borda-campo lg:hidden" aria-hidden />
        <h2 id={titleId} className="texto-h3 mb-4">{title}</h2>
        <div className="flex flex-col gap-4">{children}</div>
        {footer && <div className="mt-6 grid grid-cols-2 gap-3">{footer}</div>}
      </div>
    </div>
  )
}
