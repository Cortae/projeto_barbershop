package school.sptech.apl_api_barbershop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ClienteResponse(

        @Schema(description = "Identificador do cliente", example = "1")
        Integer id,

        @Schema(description = "Nome do cliente", example = "João Silva")
        String nome,

        @Schema(description = "Apelido do cliente", example = "João")
        String apelido,

        @Schema(description = "Celular do cliente", example = "11987654321")
        String celular,

        @Schema(description = "Data de nascimento", example = "2000-05-15")
        LocalDate nascimento,

        @Schema(description = "Aviso relacionado ao cliente", example = "Cliente cadastrado com sucesso")
        String aviso

) {
}
