package school.sptech.apl_api_barbershop.dto.bloqueio;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record BloqueioRequest(
        @NotNull Long usuarioId,
        @NotNull LocalDateTime inicio,
        @NotNull LocalDateTime fim,
        @Size(max = 100) String motivo
) {

    @AssertTrue(message = "O fim deve ser depois do início")
    public boolean isPeriodoValido() {
        return inicio == null || fim == null || fim.isAfter(inicio);
    }
}
