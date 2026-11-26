import { useQuery } from '@tanstack/react-query'
import { apiRequest } from '@/shared/api'

export const paymentKeys = { methods: ['payment-methods'] }
export const fetchPaymentMethods = () => apiRequest('/formas-pagamento')
export const usePaymentMethods = () => useQuery({
  queryKey: paymentKeys.methods,
  queryFn: fetchPaymentMethods,
  staleTime: 60_000,
})
