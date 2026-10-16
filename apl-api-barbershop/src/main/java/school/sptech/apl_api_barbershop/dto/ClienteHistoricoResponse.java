package school.sptech.apl_api_barbershop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ClienteHistoricoResponse(
        Integer idAgendamento,
        String titulo,
        LocalDateTime horarioInicio,
        LocalDateTime horarioFim,
        String status,
        BigDecimal valorPagamento,
        Boolean pago,
        List<ClienteHistoricoServicoResponse> servicos
) {
}