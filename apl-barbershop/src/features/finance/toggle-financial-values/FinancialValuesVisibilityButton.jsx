import { Eye, EyeOff } from 'lucide-react'
import { Button } from '@/shared/ui'

export default function FinancialValuesVisibilityButton({
  valuesVisible,
  onToggle,
}) {
  const label = valuesVisible
    ? 'Ocultar valores financeiros'
    : 'Mostrar valores financeiros'

  return (
    <Button
      variant="icon"
      size="icon"
      icon={valuesVisible ? Eye : EyeOff}
      aria-label={label}
      title={label}
      onClick={onToggle}
    />
  )
}
