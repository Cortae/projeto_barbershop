package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class AtualizarServicosAgendamentoRequestDto {

    @NotNull
    @Positive
    private Integer idAgendamento;
    @NotEmpty
    private List<Integer> servicos;

    public AtualizarServicosAgendamentoRequestDto(Integer idAgendamento, List<Integer> servicos) {
        this.idAgendamento = idAgendamento;
        this.servicos = servicos;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public List<Integer> getServicos() {
        return servicos;
    }

    public void setServicos(List<Integer> servicos) {
        this.servicos = servicos;
    }
}
