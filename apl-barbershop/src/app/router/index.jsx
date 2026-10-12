import { lazy, Suspense } from 'react'
import { createBrowserRouter, RouterProvider, Outlet, useLocation } from 'react-router'
import { useEffect } from 'react'
import ProtectedRoute from './ProtectedRoute'

const LoginPage = lazy(() => import('@/pages/login'))
const HomePage = lazy(() => import('@/pages/home'))
const SchedulePage = lazy(() => import('@/pages/schedule'))
const AppointmentFormPage = lazy(() => import('@/pages/appointment-form'))
const AppointmentDetailsPage = lazy(() => import('@/pages/appointment-details'))
const ClientsPage = lazy(() => import('@/pages/clients'))
const ClientPage = lazy(() => import('@/pages/client'))
const ServicesPage = lazy(() => import('@/pages/services'))
const FinancePage = lazy(() => import('@/pages/finance'))
const ExpensesPage = lazy(() => import('@/pages/expenses'))
const NotFoundPage = lazy(() => import('@/pages/not-found'))

function Root() {
  const { pathname } = useLocation()
  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])
  return (
    <Suspense fallback={<div className="min-h-dvh bg-fundo" aria-busy="true" />}>
      <Outlet />
    </Suspense>
  )
}

const router = createBrowserRouter([
  {
    element: <Root />,
    children: [
      { path: '/login', element: <LoginPage /> },
      {
        element: <ProtectedRoute />,
        children: [
          { path: '/', element: <HomePage /> },
          { path: '/agenda', element: <SchedulePage /> },
          { path: '/agendamentos/novo', element: <AppointmentFormPage /> },
          { path: '/agendamentos/:id', element: <AppointmentDetailsPage /> },
          { path: '/agendamentos/:id/editar', element: <AppointmentFormPage /> },
          { path: '/clientes', element: <ClientsPage /> },
          { path: '/clientes/novo', element: <ClientPage /> },
          { path: '/clientes/:id', element: <ClientPage /> },
          { path: '/servicos', element: <ServicesPage /> },
          { path: '/financeiro', element: <FinancePage /> },
          { path: '/financeiro/despesas', element: <ExpensesPage /> },
        ],
      },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
], { basename: import.meta.env.BASE_URL.replace(/\/$/, '') || '/' })

export default function AppRouter() {
  return <RouterProvider router={router} />
}
