package school.sptech.apl_api_barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados necessários para cadastrar um usuário")
public class UsuarioRequest {

    @Schema(description = "Nome completo do usuário",
            example = "João da Silva")
    @NotBlank
    @Size(max = 60)
    private String nome;

    @Schema(description = "E-mail do usuário",
            example = "joao@email.com")
    @NotBlank
    @Email
    @Size(max = 60)
    private String email;

    @Schema(description = "Celular com DDD, contendo 10 ou 11 dígitos",
            example = "11987654321")
    @Pattern( regexp = "^[0-9]{10,11}$")
    private String celular;

    @Schema(description = "Senha com pelo menos 6 caracteres",
            example = "senha123")
    @NotBlank
    @Size(min = 6)
    private String senha;

    @Schema(description = "ID do tipo de usuário",
            example = "1")
    @NotNull
    private Integer tipoUsuarioId;

    public UsuarioRequest() {}

    public UsuarioRequest(String nome, String email, String celular, String senha, Integer tipoUsuarioId) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.senha = senha;
        this.tipoUsuarioId = tipoUsuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getTipoUsuarioId() {
        return tipoUsuarioId;
    }

    public void setTipoUsuarioId(Integer tipoUsuarioId) {
        this.tipoUsuarioId = tipoUsuarioId;
    }
}

