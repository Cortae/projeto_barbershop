import { useId } from 'react'
import { useDialog } from './useDialog'

export default function Modal({ open, onClose, title, description, children }) {
  const ref = useDialog(open, onClose, '[data-autofocus]')
  const titleId = useId()
  if (!open) return null
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <button
        type="button"
        aria-label="Fechar modal"
        className="absolute inset-0 bg-fundo/60"
        onClick={onClose}
      />
      <div
        ref={ref}
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        className="relative z-10 w-full max-w-sm rounded-12 border border-borda bg-superficie p-4"
      >
        <h2 id={titleId} className="texto-h3">{title}</h2>
        {description && <p className="mt-2 text-texto-secundario">{description}</p>}
        <div className="mt-6 flex flex-col gap-3">{children}</div>
      </div>
    </div>
  )
}
