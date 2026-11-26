package school.sptech.apl_api_barbershop.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class RemarcarAgendamentoRequestDto {

    @NotNull
    @Positive
    @Schema(description = "Identificador do atendimento", example = "1")
    private Integer idAgendamento;

    @NotNull
    @Schema(description = "Dia e horário do inicio do atendimento", example = "2026-10-18T17:57:30")
    private LocalDateTime inicio;

    public RemarcarAgendamentoRequestDto(Integer idAgendamento, LocalDateTime inicio) {
        this.idAgendamento = idAgendamento;
        this.inicio = inicio;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }
}
