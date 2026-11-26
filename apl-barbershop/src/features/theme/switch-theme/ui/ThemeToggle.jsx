import { Moon, Sun } from 'lucide-react'
import { Button } from '@/shared/ui'
import { useTheme } from '../model/themeContext'

export default function ThemeToggle() {
  const { theme, toggleTheme } = useTheme()
  const dark = theme === 'dark'
  const label = dark ? 'Ativar tema claro' : 'Ativar tema escuro'

  return (
    <Button
      variant="icon"
      size="icon"
      icon={dark ? Sun : Moon}
      aria-label={label}
      title={label}
      onClick={toggleTheme}
    />
  )
}
