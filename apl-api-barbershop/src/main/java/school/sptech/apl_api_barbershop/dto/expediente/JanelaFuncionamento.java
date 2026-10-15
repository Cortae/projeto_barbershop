package school.sptech.apl_api_barbershop.dto.expediente;

import java.time.LocalTime;

public record JanelaFuncionamento(
        boolean aberto,
        LocalTime abertura,
        LocalTime fechamento,
        LocalTime intervaloInicio,
        LocalTime intervaloFim
) {
}
