package school.sptech.apl_api_barbershop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ClienteHistoricoServicoResponse(

        @Schema(description = "Identificador do serviço", example = "1")
        Integer idServico,

        @Schema(description = "Nome do serviço", example = "Corte masculino")
        String nomeServico,

        @Schema(description = "Valor cobrado pelo serviço", example = "35.00")
        BigDecimal valorCobrado,

        @Schema(description = "Duração do serviço em minutos", example = "30")
        Integer duracao

) {
}
