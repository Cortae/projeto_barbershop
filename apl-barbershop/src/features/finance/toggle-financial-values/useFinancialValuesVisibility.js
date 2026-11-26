import { useState } from 'react'

export const HIDDEN_FINANCIAL_VALUE = 'R$ ••••'

export const formatFinancialValue = (value, valuesVisible) =>
  valuesVisible ? value : HIDDEN_FINANCIAL_VALUE

export function useFinancialValuesVisibility(initiallyVisible = true) {
  const [valuesVisible, setValuesVisible] = useState(initiallyVisible)

  const toggleValuesVisibility = () => {
    setValuesVisible((currentValue) => !currentValue)
  }

  return { valuesVisible, toggleValuesVisibility }
}
