package school.sptech.apl_api_barbershop.dto.bloqueio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class BloqueioResponse {
    @Schema(description = "Identificador do bloqueio", example = "1")
    private Integer id;
    @Schema(description = "Identificador do usuário/barbeiro do bloqueio", example = "1")
    private Integer usuarioId;
    @Schema(description = "Data e hora de início do bloqueio", example = "2026-10-23T09:00:00")
    private LocalDateTime inicio;
    @Schema(description = "Data e hora de término do bloqueio", example = "2026-10-23T12:00:00")
    private LocalDateTime fim;
    @Schema(description = "Motivo do bloqueio", example = "Compromisso pessoal")
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
