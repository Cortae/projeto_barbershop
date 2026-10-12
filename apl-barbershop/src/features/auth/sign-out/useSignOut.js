import { useNavigate } from 'react-router'
import { useQueryClient } from '@tanstack/react-query'
import { setSession } from '@/entities/barber'
import { ROUTES } from '@/shared/config'

export function useSignOut() {
  const navigate = useNavigate()
  const qc = useQueryClient()
  return () => {
    setSession(null)
    qc.clear()
    navigate(ROUTES.login, { replace: true })
  }
}
