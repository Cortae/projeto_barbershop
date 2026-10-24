package school.sptech.apl_api_barbershop.dto.bloqueio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BloqueioRequest {
    @NotNull
    @Schema(description = "Identificador do usuário/barbeiro do bloqueio", example = "1")
    private Integer usuarioId;

    @NotNull
    @Schema(description = "Data e hora de início do bloqueio", example = "2026-10-23T09:00:00")
    private LocalDateTime inicio;

    @NotNull
    @Schema(description = "Data e hora de término do bloqueio", example = "2026-10-23T12:00:00")
    private LocalDateTime fim;

    @Size(max = 100)
    @Schema(description = "Motivo do bloqueio (até 100 caracteres)", example = "Compromisso pessoal", maxLength = 100)
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
