package school.sptech.apl_api_barbershop.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class EditarAgendamentoRequest {
    @NotNull
    @Positive
    @Schema(description = "Identificado do cliente", example = "1")
    private Integer cliente;

    @NotEmpty
    @Schema(description = "Lista de identificadores dos serviços", example = "[1,2,3]")
    private List<@NotNull @Positive Integer> servicos;

    @NotNull
    @Schema(description = "Dia e horário do inicio do atendimento", example = "2026-10-18T17:57:30")
    private LocalDateTime inicio;

    public EditarAgendamentoRequest() {
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

    public void setServicos(List<Integer> servicos) {
        this.servicos = servicos;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
}
