package school.sptech.apl_api_barbershop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ClienteHistoricoResponse(

        @Schema(description = "Identificador do agendamento", example = "1")
        Integer idAgendamento,

        @Schema(description = "Título do agendamento", example = "Corte masculino")
        String titulo,

        @Schema(description = "Data e horário de início", example = "2026-10-20T14:00:00")
        LocalDateTime horarioInicio,

        @Schema(description = "Data e horário de término", example = "2026-10-20T14:30:00")
        LocalDateTime horarioFim,

        @Schema(description = "Status do agendamento", example = "CONCLUIDO")
        String status,

        @Schema(description = "Valor do pagamento", example = "35.00")
        BigDecimal valorPagamento,

        @Schema(description = "Indica se o pagamento foi realizado", example = "true")
        Boolean pago,

        @Schema(description = "Serviços vinculados ao agendamento")
        List<ClienteHistoricoServicoResponse> servicos

) {
}
