import { apiRequest } from "@/shared/api"

export const importHolidays = (year) =>
  apiRequest(`/bloqueios/feriados/${year}`, {
    method: "POST",
  })
