package school.sptech.apl_api_barbershop.dto.expediente;

import java.time.LocalTime;

public class JanelaFuncionamento {

    private boolean aberto;
    private LocalTime abertura;
    private LocalTime fechamento;
    private LocalTime intervaloInicio;
    private LocalTime intervaloFim;

    public JanelaFuncionamento() {
    }

    public JanelaFuncionamento(
            boolean aberto,
            LocalTime abertura,
            LocalTime fechamento,
            LocalTime intervaloInicio,
            LocalTime intervaloFim
    ) {
        this.aberto = aberto;
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.intervaloInicio = intervaloInicio;
        this.intervaloFim = intervaloFim;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalTime abertura) {
        this.abertura = abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalTime fechamento) {
        this.fechamento = fechamento;
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
