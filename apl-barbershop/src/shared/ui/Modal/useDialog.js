import { useEffect, useRef } from 'react'

/** Foco preso, Esc fecha e foco volta à origem (RNF02). */
export function useDialog(open, onClose, initialFocusSelector) {
  const ref = useRef(null)
  useEffect(() => {
    if (!open) return
    const origin = document.activeElement
    const node = ref.current
    const focusables = () => [...node.querySelectorAll('button,input,select,textarea,[href],[tabindex]:not([tabindex="-1"])')].filter((el) => !el.disabled)
    ;(node.querySelector(initialFocusSelector) || focusables()[0])?.focus()
    const onKey = (e) => {
      if (e.key === 'Escape') onClose()
      if (e.key === 'Tab') {
        const list = focusables()
        const first = list[0]
        const last = list[list.length - 1]
        if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus() }
        else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus() }
      }
    }
    document.addEventListener('keydown', onKey)
    return () => {
      document.removeEventListener('keydown', onKey)
      origin?.focus?.()
    }
  }, [open]) // eslint-disable-line react-hooks/exhaustive-deps
  return ref
}
