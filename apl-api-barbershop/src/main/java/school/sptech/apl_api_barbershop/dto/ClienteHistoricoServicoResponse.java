package school.sptech.apl_api_barbershop.dto;

import java.math.BigDecimal;

public record ClienteHistoricoServicoResponse(
        Integer idServico,
        String nomeServico,
        BigDecimal valorCobrado,
        Integer duracao
) {
}