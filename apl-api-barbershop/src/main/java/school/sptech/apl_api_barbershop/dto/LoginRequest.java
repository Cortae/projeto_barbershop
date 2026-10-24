package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para realizar login")
public class LoginRequest {

    @Schema(description = "E-mail cadastrado",
            example = "admin@barbershop.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Senha do usuário",
            example = "senha123")
    @NotBlank
    private String senha;

    public LoginRequest() {
    }

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
