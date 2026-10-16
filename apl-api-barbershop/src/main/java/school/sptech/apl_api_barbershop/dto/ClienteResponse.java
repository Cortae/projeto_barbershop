package school.sptech.apl_api_barbershop.dto;

import java.time.LocalDate;

public record ClienteResponse(
        Integer id,
        String nome,
        String apelido,
        String celular,
        LocalDate nascimento,
        String aviso
) {
}
