
package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicoRequest(

        @NotBlank
        @Size(max = 45)
        String titulo,

        @NotNull
        @Positive
        BigDecimal valorAtual,

        @NotNull
        @Positive
        Integer duracaoAtual

) {
}

