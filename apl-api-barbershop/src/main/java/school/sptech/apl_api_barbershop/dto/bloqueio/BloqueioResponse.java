package school.sptech.apl_api_barbershop.dto.bloqueio;

import java.time.LocalDateTime;

public class BloqueioResponse {
    private Integer id;
    private Integer usuarioId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String motivo;

    public BloqueioResponse() {
    }

    public BloqueioResponse(Integer id, Integer usuarioId, LocalDateTime inicio,
                            LocalDateTime fim, String motivo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.inicio = inicio;
        this.fim = fim;
        this.motivo = motivo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
