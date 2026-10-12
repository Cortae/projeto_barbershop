import { useCallback, useState } from 'react'
import { CheckCircle2, AlertCircle } from 'lucide-react'
import { ToastContext } from './toastContext'

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([])
  const show = useCallback((message, type = 'success') => {
    const id = Math.random()
    setToasts((t) => [...t, { id, message, type }])
    setTimeout(() => setToasts((t) => t.filter((x) => x.id !== id)), 4000)
  }, [])
  return (
    <ToastContext.Provider value={show}>
      {children}
      <div className="pointer-events-none fixed inset-x-4 bottom-20 z-[60] flex flex-col items-center gap-2 lg:bottom-8 lg:left-auto lg:right-8 lg:items-end">
        {toasts.map((t) => (
          <div
            key={t.id}
            role={t.type === 'error' ? 'alert' : 'status'}
            className="flex w-full max-w-md items-center gap-3 rounded-12 border border-borda-campo bg-superficie-elevada p-3"
          >
            {t.type === 'error' ? <AlertCircle className="size-6 shrink-0 text-erro" aria-hidden /> : <CheckCircle2 className="size-6 shrink-0 text-sucesso" aria-hidden />}
            <span>{t.message}</span>
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  )
}
