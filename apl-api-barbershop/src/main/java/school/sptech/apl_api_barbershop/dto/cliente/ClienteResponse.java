package school.sptech.apl_api_barbershop.dto.cliente;

import school.sptech.apl_api_barbershop.entity.Cliente;

import java.time.LocalDate;

public class ClienteResponse {
    private Integer id;
    private String nome;
    private String apelido;
    private String telefone;
    private LocalDate nascimento;

    public ClienteResponse() {
    }

    public ClienteResponse(Integer id, String nome, String apelido, String telefone, LocalDate nascimento) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getApelido(),
                cliente.getTelefone(), cliente.getNascimento());
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getApelido() { return apelido; }
    public void setApelido(String apelido) { this.apelido = apelido; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getNascimento() { return nascimento; }
    public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }
}
