import { ChevronLeft, ChevronRight } from 'lucide-react'
import Button from '../button/Button'

export default function PeriodSelector({ label, onPrev, onNext, prevLabel = 'Anterior', nextLabel = 'Próximo', disabled }) {
  return (
    <div className="flex items-center justify-between gap-2">
      <Button variant="icon" size="icon" icon={ChevronLeft} aria-label={prevLabel} onClick={onPrev} disabled={disabled} />
      <span className="texto-h3 text-center" aria-live="polite">{label}</span>
      <Button variant="icon" size="icon" icon={ChevronRight} aria-label={nextLabel} onClick={onNext} disabled={disabled} />
    </div>
  )
}
