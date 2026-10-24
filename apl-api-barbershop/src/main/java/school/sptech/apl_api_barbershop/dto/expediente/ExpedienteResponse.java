package school.sptech.apl_api_barbershop.dto.expediente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class ExpedienteResponse {

    @Schema(description = "Identificador do usuário/barbeiro", example = "1")
    private Integer usuarioId;
    @Schema(description = "Expediente configurado para cada dia da semana")
    private List<DiaExpedienteResponse> dias;

    public ExpedienteResponse() {
    }

    public ExpedienteResponse(Integer usuarioId, List<DiaExpedienteResponse> dias) {
        this.usuarioId = usuarioId;
        this.dias = dias;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DiaExpedienteResponse> getDias() {
        return dias;
    }

    public void setDias(List<DiaExpedienteResponse> dias) {
        this.dias = dias;
    }
}
