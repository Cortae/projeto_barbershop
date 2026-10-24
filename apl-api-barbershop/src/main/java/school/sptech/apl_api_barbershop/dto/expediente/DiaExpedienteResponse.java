package school.sptech.apl_api_barbershop.dto.expediente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public class DiaExpedienteResponse {

    @Schema(description = "Dia da semana: 1=segunda-feira, ..., 7=domingo", example = "1")
    private Integer diaSemana;
    @Schema(description = "Indica se o estabelecimento atende neste dia", example = "true")
    private boolean aberto;
    @Schema(description = "Horário de abertura", example = "09:00:00")
    private LocalTime horaAbertura;
    @Schema(description = "Horário de fechamento", example = "18:00:00")
    private LocalTime horaFechamento;
    @Schema(description = "Início do intervalo", example = "12:00:00")
    private LocalTime intervaloInicio;
    @Schema(description = "Fim do intervalo", example = "13:00:00")
    private LocalTime intervaloFim;

    public DiaExpedienteResponse() {
    }

    public DiaExpedienteResponse(
            Integer diaSemana,
            boolean aberto,
            LocalTime horaAbertura,
            LocalTime horaFechamento,
            LocalTime intervaloInicio,
            LocalTime intervaloFim
    ) {
        this.diaSemana = diaSemana;
        this.aberto = aberto;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.intervaloInicio = intervaloInicio;
        this.intervaloFim = intervaloFim;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public LocalTime getIntervaloInicio() {
        return intervaloInicio;
    }

    public void setIntervaloInicio(LocalTime intervaloInicio) {
        this.intervaloInicio = intervaloInicio;
    }

    public LocalTime getIntervaloFim() {
        return intervaloFim;
    }

    public void setIntervaloFim(LocalTime intervaloFim) {
        this.intervaloFim = intervaloFim;
    }
}
