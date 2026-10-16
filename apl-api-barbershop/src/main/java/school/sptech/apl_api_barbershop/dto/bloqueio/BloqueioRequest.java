package school.sptech.apl_api_barbershop.dto.bloqueio;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BloqueioRequest {
    @NotNull
    private Integer usuarioId;

    @NotNull
    private LocalDateTime inicio;

    @NotNull
    private LocalDateTime fim;

    @Size(max = 100)
    private String motivo;

    public BloqueioRequest() {
    }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
