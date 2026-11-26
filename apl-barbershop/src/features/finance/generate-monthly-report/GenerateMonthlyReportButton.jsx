import { useState } from 'react'
import { FileDown } from 'lucide-react'
import { Button, useToast } from '@/shared/ui'
import { downloadMonthlyFinanceReport } from './reportApi'

export default function GenerateMonthlyReportButton({ month, summary, disabled = false }) {
  const [isGenerating, setIsGenerating] = useState(false)
  const toast = useToast()

  const handleClick = async () => {
    setIsGenerating(true)
    try {
      await downloadMonthlyFinanceReport(month, summary)
      toast('Relatório mensal gerado com sucesso.')
    } catch (error) {
      toast(error.message, 'error')
    } finally {
      setIsGenerating(false)
    }
  }

  return (
    <Button
      icon={FileDown}
      loading={isGenerating}
      loadingLabel="Gerando PDF..."
      disabled={disabled || !summary}
      onClick={handleClick}
    >
      Gerar relatório mensal
    </Button>
  )
}
