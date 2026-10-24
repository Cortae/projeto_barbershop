
package school.sptech.apl_api_barbershop.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtivoRequest(

        @Schema(description = "Indica se o serviço deve ficar ativo ou inativo", example = "true")
        @NotNull
        Boolean ativo

) {
}
