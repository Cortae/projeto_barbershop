package school.sptech.apl_api_barbershop.dto.bloqueio;

import java.time.LocalDateTime;

public record BloqueioResponse(
        Long id,
        Long usuarioId,
        LocalDateTime inicio,
        LocalDateTime fim,
        String motivo
) {
}
