
package school.sptech.apl_api_barbershop.dto;

import java.math.BigDecimal;

public record ServicoResponse(
        Integer id,
        String titulo,
        BigDecimal valorAtual,
        Integer duracaoAtual,
        boolean ativo
) {
}

