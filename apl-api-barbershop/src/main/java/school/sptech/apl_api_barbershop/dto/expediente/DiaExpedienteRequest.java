package school.sptech.apl_api_barbershop.dto.expediente;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class DiaExpedienteRequest {

    @NotNull
    @Min(1)
    @Max(7)
    private Integer diaSemana;

    @NotNull
    private Boolean aberto;

    private LocalTime horaAbertura;
    private LocalTime horaFechamento;
    private LocalTime intervaloInicio;
    private LocalTime intervaloFim;

    public DiaExpedienteRequest() {
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Boolean getAberto() {
        return aberto;
    }

    public void setAberto(Boolean aberto) {
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

    public boolean configuracaoValida() {
        if (aberto == null || diaSemana == null || diaSemana < 1 || diaSemana > 7) {
            return false;
        }

        if (!aberto) {
            return horaAbertura == null && horaFechamento == null
                    && intervaloInicio == null && intervaloFim == null;
        }

        if (horaAbertura == null || horaFechamento == null
                || !horaAbertura.isBefore(horaFechamento)) {
            return false;
        }

        if ((intervaloInicio == null) != (intervaloFim == null)) {
            return false;
        }

        return intervaloInicio == null
                || (intervaloInicio.isBefore(intervaloFim)
                && !intervaloInicio.isBefore(horaAbertura)
                && !intervaloFim.isAfter(horaFechamento));
    }
}
