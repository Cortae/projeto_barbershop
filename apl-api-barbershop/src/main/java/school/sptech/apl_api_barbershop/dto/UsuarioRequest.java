package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.*;

public class UsuarioRequest {

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 60)
    private String email;

    @Pattern( regexp = "^[0-9]{10,11}$")
    private String celular;

    @NotBlank
    @Size(min = 6)
    private String senha;

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
