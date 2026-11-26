import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from '@/shared/api'
import { ToastProvider } from '@/shared/ui'
import { ThemeProvider } from '@/features/theme/switch-theme'

export default function Providers({ children }) {
  return (
    <ThemeProvider>
      <QueryClientProvider client={queryClient}>
        <ToastProvider>{children}</ToastProvider>
      </QueryClientProvider>
    </ThemeProvider>
  )
}
