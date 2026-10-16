package school.sptech.apl_api_barbershop.dto.expediente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ExpedienteRequest {

    @NotNull
    @Size(min = 7, max = 7)
    @Valid
    private List<DiaExpedienteRequest> dias;

    public ExpedienteRequest() {
    }

    public List<DiaExpedienteRequest> getDias() {
        return dias;
    }

    public void setDias(List<DiaExpedienteRequest> dias) {
        this.dias = dias;
    }
}
