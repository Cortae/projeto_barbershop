package school.sptech.apl_api_barbershop.dto.expediente;

import java.util.List;

public class ExpedienteResponse {

    private Integer usuarioId;
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
