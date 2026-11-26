package school.sptech.apl_api_barbershop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteItemResponse(

        @Schema(description = "Identificador do cliente", example = "1")
        Integer id,

        @Schema(description = "Nome do cliente", example = "João Silva")
        String nome,

        @Schema(description = "Celular do cliente", example = "11987654321")
        String celular,

        @Schema(description = "Data e horário do último corte", example = "2026-10-15T14:30:00")
        LocalDateTime ultimoCorte,

        @Schema(description = "Valor total pendente de pagamento", example = "50.00")
        BigDecimal totalPendente

) {
}
