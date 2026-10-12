import { useNavigate } from 'react-router'
import { Button } from '@/shared/ui'
import { ROUTES } from '@/shared/config'

export default function RescheduleButton({ appointmentId, disabled }) {
  const navigate = useNavigate()
  return (
    <Button variant="secondary" full disabled={disabled} onClick={() => navigate(ROUTES.appointmentEdit(appointmentId))}>
      Remarcar
    </Button>
  )
}
