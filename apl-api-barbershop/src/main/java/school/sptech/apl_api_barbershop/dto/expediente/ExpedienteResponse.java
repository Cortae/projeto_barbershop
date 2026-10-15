package school.sptech.apl_api_barbershop.dto.expediente;

import java.util.List;

public record ExpedienteResponse(Integer usuarioId, List<DiaExpedienteResponse> dias) {
}
