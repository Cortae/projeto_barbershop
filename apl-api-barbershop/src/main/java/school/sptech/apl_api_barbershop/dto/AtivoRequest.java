
package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.NotNull;

public record AtivoRequest(

        @NotNull
        Boolean ativo

) {
}

