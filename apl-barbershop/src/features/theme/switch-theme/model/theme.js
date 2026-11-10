export const THEME_STORAGE_KEY = 'jr-theme'
export const THEMES = ['light', 'dark']

export const isTheme = (value) => THEMES.includes(value)

export const resolveTheme = (storedTheme, prefersDark = false) =>
  isTheme(storedTheme) ? storedTheme : prefersDark ? 'dark' : 'light'

export const readStoredTheme = () => {
  try {
    const value = localStorage.getItem(THEME_STORAGE_KEY)
    return isTheme(value) ? value : null
  } catch {
    return null
  }
}

export const getSystemTheme = () =>
  typeof window !== 'undefined' && window.matchMedia?.('(prefers-color-scheme: dark)').matches
    ? 'dark'
    : 'light'

export const getInitialTheme = () => {
  const appliedTheme = typeof document !== 'undefined' ? document.documentElement.dataset.theme : null
  return isTheme(appliedTheme) ? appliedTheme : resolveTheme(readStoredTheme(), getSystemTheme() === 'dark')
}

export const applyTheme = (theme) => {
  document.documentElement.dataset.theme = theme
  document.documentElement.style.colorScheme = theme
}

export const persistTheme = (theme) => {
  try {
    localStorage.setItem(THEME_STORAGE_KEY, theme)
  } catch {
    // A troca continua funcionando durante a sessão quando o armazenamento não está disponível.
  }
}
