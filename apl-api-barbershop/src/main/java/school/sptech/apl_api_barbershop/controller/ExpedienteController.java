package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteRequest;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteResponse;
import school.sptech.apl_api_barbershop.service.ExpedienteService;

@Tag(name = "Expedientes", description = "Operações sobre expedientes.")
@RestController
@RequestMapping("/usuarios/{usuarioId}/expediente")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    public ExpedienteController(ExpedienteService expedienteService) {
        this.expedienteService = expedienteService;
    }

    @Operation(
            summary = "Configuração de expediente",
            description = "Configurar o expediente para o usuário especificado. O expediente é configurado via Corpo de requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expediente configurado",
                    content = @Content(schema = @Schema(implementation = ExpedienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Configuração inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping
    public ResponseEntity<ExpedienteResponse> configurar(
            @Parameter(description = "Identificador do usuário/barbeiro", example = "1")
            @PathVariable Integer usuarioId,
            @Valid @RequestBody ExpedienteRequest request
    ) {
        return ResponseEntity.ok(expedienteService.configurar(usuarioId, request));
    }

    @Operation(
            summary = "Consulta de expediente",
            description = "Consultar o expediente do usuário especificado via ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expediente do usuário",
                    content = @Content(schema = @Schema(implementation = ExpedienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<ExpedienteResponse> consultar(
            @Parameter(description = "Identificador do usuário/barbeiro", example = "1")
            @PathVariable Integer usuarioId) {
        return ResponseEntity.ok(expedienteService.consultar(usuarioId));
    }
}
