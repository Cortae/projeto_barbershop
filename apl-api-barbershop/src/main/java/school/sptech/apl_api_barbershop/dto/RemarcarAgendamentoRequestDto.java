package school.sptech.apl_api_barbershop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class RemarcarAgendamentoRequestDto {

    @NotNull
    @Positive
    private Integer idAgendamento;
    @NotNull
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
