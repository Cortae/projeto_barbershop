package school.sptech.apl_api_barbershop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de um usuário")
public class UsuarioResponse {

    @Schema(description = "Identificador do usuário",
            example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário",
            example = "João da Silva")
    private String nome;

    @Schema(description = "E-mail do usuário",
            example = "joao@email.com")
    private String email;

    @Schema(description = "Tipo de usuário",
            example = "ADMINISTRADOR")
    private String tipoUsuario;

    public UsuarioResponse() {}

    public UsuarioResponse(Integer id, String nome, String email, String tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
