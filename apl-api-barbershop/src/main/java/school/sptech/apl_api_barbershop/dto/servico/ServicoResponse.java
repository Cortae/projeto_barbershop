
package school.sptech.apl_api_barbershop.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ServicoResponse(

        @Schema(description = "Identificador do serviço", example = "1")
        Integer id,

        @Schema(description = "Título do serviço", example = "Corte masculino")
        String titulo,

        @Schema(description = "Valor atual do serviço em reais", example = "35.00")
        BigDecimal valorAtual,

        @Schema(description = "Duração do serviço em minutos", example = "30")
        Integer duracaoAtual,

        @Schema(description = "Indica se o serviço está ativo", example = "true")
        boolean ativo

) {
}
