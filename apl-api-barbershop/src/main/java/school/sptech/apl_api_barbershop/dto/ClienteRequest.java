package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteRequest(

        @NotBlank
        @Size(max = 60)
        String nome,

        @Size(max = 45)
        String apelido,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}")
        String celular,

        @Past
        LocalDate nascimento

) {
}