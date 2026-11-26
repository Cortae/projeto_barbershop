package school.sptech.apl_api_barbershop.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicoRequest(

        @Schema(description = "Título do serviço", example = "Corte masculino")
        @NotBlank
        @Size(max = 45)
        String titulo,

        @Schema(description = "Valor atual do serviço em reais", example = "35.00")
        @NotNull
        @Positive
        BigDecimal valorAtual,

        @Schema(description = "Duração do serviço em minutos", example = "30")
        @NotNull
        @Positive
        Integer duracaoAtual

) {
}
