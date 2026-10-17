package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteRequest;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteResponse;
import school.sptech.apl_api_barbershop.service.ExpedienteService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/expediente")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    public ExpedienteController(ExpedienteService expedienteService) {
        this.expedienteService = expedienteService;
    }

    @PutMapping
    public ResponseEntity<ExpedienteResponse> configurar(
            @PathVariable Integer usuarioId,
            @Valid @RequestBody ExpedienteRequest request
    ) {
        return ResponseEntity.ok(expedienteService.configurar(usuarioId, request));
    }

    @GetMapping
    public ResponseEntity<ExpedienteResponse> consultar(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(expedienteService.consultar(usuarioId));
    }
}
