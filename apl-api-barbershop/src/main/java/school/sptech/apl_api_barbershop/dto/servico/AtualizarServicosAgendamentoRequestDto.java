package school.sptech.apl_api_barbershop.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class AtualizarServicosAgendamentoRequestDto {

    @NotNull
    @Positive
    @Schema(description = "Identificador do atendimento", example = "1")
    private Integer idAgendamento;

    @NotEmpty
    @Schema(description = "Lista de identificadores dos serviços", example = "[1,2,3]")
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
