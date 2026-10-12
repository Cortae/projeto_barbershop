import { Navigate, Outlet, useLocation } from 'react-router'
import { useSession } from '@/entities/barber'
import { ROUTES } from '@/shared/config'

/** Sem sessão → TL01 (RNF06). */
export default function ProtectedRoute() {
  const session = useSession()
  const location = useLocation()
  if (!session) return <Navigate to={ROUTES.login} replace state={{ from: location }} />
  return <Outlet />
}
