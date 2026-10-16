package school.sptech.apl_api_barbershop.dto.expediente;

import java.time.LocalTime;

public class DiaExpedienteResponse {

    private Integer diaSemana;
    private boolean aberto;
    private LocalTime horaAbertura;
    private LocalTime horaFechamento;
    private LocalTime intervaloInicio;
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
