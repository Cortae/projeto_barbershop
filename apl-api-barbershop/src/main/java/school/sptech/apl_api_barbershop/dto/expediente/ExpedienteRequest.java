package school.sptech.apl_api_barbershop.dto.expediente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ExpedienteRequest(
        @NotNull @Size(min = 7, max = 7) @Valid List<DiaExpedienteRequest> dias
) {
}
