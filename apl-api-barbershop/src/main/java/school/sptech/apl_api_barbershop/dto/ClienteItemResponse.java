package school.sptech.apl_api_barbershop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteItemResponse(
        Integer id,
        String nome,
        String celular,
        LocalDateTime ultimoCorte,
        BigDecimal totalPendente
) {
}