import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useToast } from '@/shared/ui'

/** Mutation com invalidação de cache e toast de feedback (RNF05). */
export function useAppMutation({ mutationFn, invalidate = [], successMessage, errorMessage = 'Não foi possível salvar. Tente novamente.', onSuccess, suppressConflict = false }) {
  const qc = useQueryClient()
  const toast = useToast()
  return useMutation({
    mutationFn,
    onSuccess: async (data, vars) => {
      await Promise.all(invalidate.map((key) => qc.invalidateQueries({ queryKey: [key] })))
      const msg = typeof successMessage === 'function' ? successMessage(data, vars) : successMessage
      if (msg) toast(msg)
      onSuccess?.(data, vars)
    },
    onError: (err) => {
      if (err?.code !== 'conflict' || !suppressConflict) toast(err?.message || errorMessage, 'error')
    },
  })
}
