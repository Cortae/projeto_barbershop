package school.sptech.apl_api_barbershop.dto.expediente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ExpedienteRequest {

    @NotNull
    @Size(min = 7, max = 7)
    @Valid
    @Schema(description = "Configuração dos sete dias da semana, com cada dia informado uma única vez")
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
