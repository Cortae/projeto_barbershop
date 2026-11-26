import { useEffect, useRef, useState } from 'react'
import { LogOut } from 'lucide-react'
import { Avatar } from '@/shared/ui'
import { useSession } from '@/entities/barber'
import { useSignOut } from '@/features/auth/sign-out'

export default function AvatarMenu() {
  const session = useSession()
  const signOut = useSignOut()
  const [open, setOpen] = useState(false)
  const ref = useRef(null)
  useEffect(() => {
    if (!open) return
    const close = (e) => !ref.current?.contains(e.target) && setOpen(false)
    const esc = (e) => e.key === 'Escape' && setOpen(false)
    document.addEventListener('mousedown', close)
    document.addEventListener('keydown', esc)
    return () => {
      document.removeEventListener('mousedown', close)
      document.removeEventListener('keydown', esc)
    }
  }, [open])
  return (
    <div ref={ref} className="relative">
      <Avatar name={session?.nome} aria-label="Menu da conta" aria-haspopup="menu" aria-expanded={open} onClick={() => setOpen((o) => !o)} />
      {open && (
        <div role="menu" className="absolute right-0 top-full z-40 mt-2 w-48 rounded-12 border border-borda bg-superficie-elevada p-2">
          <p className="texto-corpo-pequeno truncate px-3 py-2 text-texto-secundario">{session?.email}</p>
          <button role="menuitem" onClick={signOut} className="flex min-h-11 w-full items-center gap-2 rounded-8 px-3 hover:bg-superficie">
            <LogOut className="size-5" aria-hidden /> Sair
          </button>
        </div>
      )}
    </div>
  )
}
