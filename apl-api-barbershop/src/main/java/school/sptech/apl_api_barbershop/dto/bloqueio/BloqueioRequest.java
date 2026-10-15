package school.sptech.apl_api_barbershop.dto.bloqueio;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record BloqueioRequest(
        @NotNull Integer usuarioId,
        @NotNull LocalDateTime inicio,
        @NotNull LocalDateTime fim,
        @Size(max = 100) String motivo
) {
}
