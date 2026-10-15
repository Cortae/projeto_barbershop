package school.sptech.apl_api_barbershop.dto.expediente;

import java.time.LocalTime;

public record DiaExpedienteResponse(
        Integer diaSemana,
        boolean aberto,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        LocalTime intervaloInicio,
        LocalTime intervaloFim
) {
}
