package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.*;
import school.sptech.apl_api_barbershop.entity.Servico;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCadastroRequestDto {

    @NotNull
    @Min(1)
    private Integer barbeiro;
    @NotNull
    @Min(1)
    private Integer cliente;
    @NotNull
    @NotEmpty
    private List<Integer> servicos;
    @NotNull
    private LocalDateTime inicio;

    public AgendamentoCadastroRequestDto(Integer barbeiro, Integer cliente, List<Integer> servicos, LocalDateTime inicio) {
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.servicos = servicos;
        this.inicio = inicio;
    }

    public Integer getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Integer barbeiro) {
        this.barbeiro = barbeiro;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public List<Integer> getServicos() {
        return servicos;
    }

    public void setServicos(List<Integer> servico) {
        this.servicos = servico;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
}
