import { useState } from "react"

import { BottomSheet, Button, TextField, Banner } from "@/shared/ui"

import { useAppMutation } from "@/shared/lib/mutation"

import { importHolidays } from "./importHolidays"

const currentYear = new Date().getFullYear()

export default function ImportHolidaysSheet({ open, onClose }) {
  const [year, setYear] = useState(String(currentYear))

  const [error, setError] = useState(null)

  const [result, setResult] = useState(null)

  const mutation = useAppMutation({
    mutationFn: importHolidays,

    invalidate: ["schedule"],

    successMessage: "Feriados importados para a agenda.",

    errorMessage: "Não foi possível importar os feriados.",

    onSuccess: setResult,
  })

  const busy = mutation.isPending

  const changeYear = (event) => {
    setYear(event.target.value)

    setError(null)

    setResult(null)
  }

  const submit = () => {
    const parsedYear = Number(year)

    if (
      !Number.isInteger(parsedYear) ||
      parsedYear < 1900 ||
      parsedYear > 2199
    ) {
      setError("Informe um ano entre 1900 e 2199.")

      return
    }

    mutation.mutate(parsedYear)
  }

  return (
    <BottomSheet
      open={open}
      onClose={() => !busy && onClose()}
      title="Importar feriados"
      footer={
        <>
          <Button variant="secondary" disabled={busy} onClick={onClose}>
            Fechar
          </Button>
          <Button loading={busy} loadingLabel="Importando..." onClick={submit}>
            {result ? "Importar novamente" : "Importar"}
          </Button>
        </>
      }
    >
      <p className="texto-corpo-pequeno text-texto-secundario">
        Os feriados nacionais serão bloqueados durante o dia inteiro para todos
        os barbeiros. Bloqueios existentes não serão duplicados.
      </p>
      <TextField
        label="Ano"
        required
        type="number"
        min="1900"
        max="2199"
        step="1"
        value={year}
        onChange={changeYear}
        error={error}
        disabled={busy}
      />
      {result && (
        <Banner variant="info">
          <span className="flex flex-col gap-1">
            <span>
              {result.feriadosEncontrados} feriados encontrados para{" "}
              {result.barbeiros} barbeiros.
            </span>
            <span>{result.bloqueiosCriados} bloqueios criados.</span>
            <span>{result.bloqueiosJaExistentes} bloqueios já existentes.</span>
            <span>{result.conflitos.length} conflitos com agendamentos.</span>
          </span>
        </Banner>
      )}
      {result?.conflitos.length > 0 && (
        <div className="flex flex-col gap-2">
          <h3 className="texto-label">Conflitos encontrados</h3>
          <ul className="flex max-h-40 flex-col gap-2 overflow-y-auto texto-corpo-pequeno text-texto-secundario">
            {result.conflitos.map((conflict) => (
              <li key={`${conflict.barbeiroId}-${conflict.data}`}>
                Barbeiro {conflict.barbeiroId}: {conflict.feriado} em{" "}
                {conflict.data}
              </li>
            ))}
          </ul>
        </div>
      )}
    </BottomSheet>
  )
}
