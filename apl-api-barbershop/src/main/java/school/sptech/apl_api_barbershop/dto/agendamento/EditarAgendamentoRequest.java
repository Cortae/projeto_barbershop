package school.sptech.apl_api_barbershop.dto.agendamento;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class EditarAgendamentoRequest {
    @NotNull
    @Positive
    private Integer cliente;

    @NotEmpty
    private List<@NotNull @Positive Integer> servicos;

    @NotNull
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
