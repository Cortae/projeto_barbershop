import { useSyncExternalStore } from 'react'

const KEY = 'jr-session'
const listeners = new Set()
let session = JSON.parse(localStorage.getItem(KEY) || 'null')

const emit = () => listeners.forEach((l) => l())
export const setSession = (value) => {
  session = value
  value ? localStorage.setItem(KEY, JSON.stringify(value)) : localStorage.removeItem(KEY)
  emit()
}
export const getSession = () => session
export const useSession = () =>
  useSyncExternalStore((l) => (listeners.add(l), () => listeners.delete(l)), getSession)
