package school.sptech.apl_api_barbershop.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoCadastroRequestDto {

    @NotNull
    @Min(1)
    @Schema(description = "Identificado do barbeiro", example = "1")
    private Integer barbeiro;

    @NotNull
    @Min(1)
    @Schema(description = "Identificado do cliente", example = "1")
    private Integer cliente;

    @NotNull
    @NotEmpty
    @Schema(description = "Lista com identificadores dos serviços", example = "[1,2,3]")
    private List<Integer> servicos;

    @NotNull
    @Schema(description = "Dia e horário do inicio do atendimento", example = "2026-10-18T17:57:30")
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
