package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RecuperarSenhaRequestDto {

    @NotBlank
    @Email
    private String email;

    public RecuperarSenhaRequestDto() {
    }

    public RecuperarSenhaRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
