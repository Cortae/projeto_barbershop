import { useCallback, useEffect, useMemo, useState } from 'react'
import { ThemeContext } from './themeContext'
import {
  THEME_STORAGE_KEY,
  applyTheme,
  getInitialTheme,
  getSystemTheme,
  isTheme,
  persistTheme,
  readStoredTheme,
} from './theme'

export default function ThemeProvider({ children }) {
  const [theme, setThemeState] = useState(getInitialTheme)

  const setTheme = useCallback((nextTheme) => {
    if (!isTheme(nextTheme)) return
    applyTheme(nextTheme)
    persistTheme(nextTheme)
    setThemeState(nextTheme)
  }, [])

  const toggleTheme = useCallback(() => {
    setTheme(theme === 'dark' ? 'light' : 'dark')
  }, [setTheme, theme])

  useEffect(() => {
    applyTheme(theme)
  }, [theme])

  useEffect(() => {
    const media = window.matchMedia('(prefers-color-scheme: dark)')
    const handleSystemChange = (event) => {
      if (!readStoredTheme()) setThemeState(event.matches ? 'dark' : 'light')
    }
    const handleStorage = (event) => {
      if (event.key !== THEME_STORAGE_KEY) return
      setThemeState(isTheme(event.newValue) ? event.newValue : getSystemTheme())
    }
    media.addEventListener('change', handleSystemChange)
    window.addEventListener('storage', handleStorage)
    return () => {
      media.removeEventListener('change', handleSystemChange)
      window.removeEventListener('storage', handleStorage)
    }
  }, [])

  const value = useMemo(() => ({ theme, setTheme, toggleTheme }), [setTheme, theme, toggleTheme])

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
}
