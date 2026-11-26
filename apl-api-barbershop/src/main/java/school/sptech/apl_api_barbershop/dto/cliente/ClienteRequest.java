package school.sptech.apl_api_barbershop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteRequest(

        @Schema(description = "Nome completo do cliente", example = "João Silva")
        @NotBlank
        @Size(max = 60)
        String nome,

        @Schema(description = "Apelido do cliente", example = "João")
        @Size(max = 45)
        String apelido,

        @Schema(description = "Celular do cliente com DDD, apenas números", example = "11987654321")
        @NotBlank
        @Pattern(regexp = "\\d{10,11}")
        String celular,

        @Schema(description = "Data de nascimento do cliente", example = "2000-05-15")
        @Past
        LocalDate nascimento

) {
}