package school.sptech.apl_api_barbershop.dto.expediente;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record DiaExpedienteRequest(
        @NotNull @Min(1) @Max(7) Integer diaSemana,
        @NotNull Boolean aberto,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        LocalTime intervaloInicio,
        LocalTime intervaloFim
) {

    public boolean configuracaoValida() {
        if (aberto == null || diaSemana == null || diaSemana < 1 || diaSemana > 7) {
            return false;
        }

        if (!aberto) {
            return horaAbertura == null && horaFechamento == null
                    && intervaloInicio == null && intervaloFim == null;
        }

        if (horaAbertura == null || horaFechamento == null
                || !horaAbertura.isBefore(horaFechamento)) {
            return false;
        }

        if ((intervaloInicio == null) != (intervaloFim == null)) {
            return false;
        }

        return intervaloInicio == null
                || (intervaloInicio.isBefore(intervaloFim)
                && !intervaloInicio.isBefore(horaAbertura)
                && !intervaloFim.isAfter(horaFechamento));
    }
}
