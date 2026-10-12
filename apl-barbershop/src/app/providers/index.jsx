import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from '@/shared/api'
import { ToastProvider } from '@/shared/ui'

export default function Providers({ children }) {
  return (
    <QueryClientProvider client={queryClient}>
      <ToastProvider>{children}</ToastProvider>
    </QueryClientProvider>
  )
}
