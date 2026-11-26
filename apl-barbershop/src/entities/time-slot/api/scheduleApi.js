import { useQuery } from "@tanstack/react-query"
import { apiRequest } from "@/shared/api"

export const scheduleKeys = {
  all: ["schedule"],
  blocks: (from, to, barberId = 2) => [
    "schedule",
    "blocks",
    from,
    to,
    barberId,
  ],
  hours: ["schedule", "hours"],
}

const mapBlock = (block) => ({
  id: block.id,
  barbeiroId: block.usuarioId,
  data: block.inicio.slice(0, 10),
  inicio: block.inicio.slice(11, 16),
  fim: block.fim.slice(11, 16),
  motivo: block.motivo,
})

const frontendDay = (backendDay) => backendDay % 7
const shortTime = (value) => value?.slice(0, 5) ?? null

const mapWorkingHours = (response) => {
  const openDays = response.dias.filter((day) => day.aberto)
  const reference = openDays[0]
  return {
    usuarioId: response.usuarioId,
    dias: openDays.map((day) => frontendDay(day.diaSemana)).sort(),
    horarioAbertura: shortTime(reference?.horaAbertura) ?? "09:00",
    horarioFechamento: shortTime(reference?.horaFechamento) ?? "18:00",
    inicioIntervalo: shortTime(reference?.intervaloInicio),
    fimIntervalo: shortTime(reference?.intervaloFim),
  }
}

export const fetchBlocks = (from, to, barberId = 2) =>
  apiRequest(
    `/bloqueios?barbeiroId=${barberId}&inicio=${encodeURIComponent(`${from}T00:00:00`)}&fim=${encodeURIComponent(`${to}T23:59:59`)}`,
  ).then((items) => items.map(mapBlock))

export const fetchWorkingHours = (barberId = 2) =>
  apiRequest(`/usuarios/${Number(barberId)}/expediente`).then(mapWorkingHours)

export const createBlock = (block) =>
  apiRequest("/bloqueios", {
    method: "POST",
    body: JSON.stringify({
      usuarioId: block.barbeiroId ?? 2,
      inicio: `${block.data}T${block.inicio}:00`,
      fim: `${block.data}T${block.fim}:00`,
      motivo: block.motivo || null,
    }),
  }).then(mapBlock)

export const deleteBlock = (id) =>
  apiRequest(`/bloqueios/${Number(id)}`, { method: "DELETE" })

export const saveWorkingHours = (hours) =>
  apiRequest(`/usuarios/${Number(hours.usuarioId ?? 2)}/expediente`, {
    method: "PUT",
    body: JSON.stringify({
      dias: Array.from({ length: 7 }, (_, index) => index + 1).map(
        (diaSemana) => {
          const aberto = hours.dias.includes(frontendDay(diaSemana))
          return {
            diaSemana,
            aberto,
            horaAbertura: aberto ? hours.horarioAbertura : null,
            horaFechamento: aberto ? hours.horarioFechamento : null,
            intervaloInicio: aberto ? hours.inicioIntervalo || null : null,
            intervaloFim: aberto ? hours.fimIntervalo || null : null,
          }
        },
      ),
    }),
  }).then(mapWorkingHours)

export const useBlocks = (from, to, barberId = 2) =>
  useQuery({
    queryKey: scheduleKeys.blocks(from, to, barberId),
    queryFn: () => fetchBlocks(from, to, barberId),
    enabled: Boolean(from && to),
  })

export const useWorkingHours = (barberId = 2) =>
  useQuery({
    queryKey: [...scheduleKeys.hours, barberId],
    queryFn: () => fetchWorkingHours(barberId),
  })
