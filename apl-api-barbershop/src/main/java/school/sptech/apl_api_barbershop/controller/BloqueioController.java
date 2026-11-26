package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioRequest;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioResponse;
import school.sptech.apl_api_barbershop.service.BloqueioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Bloqueios", description = "Operações sobre bloqueios.")
@RestController
@Validated
@RequestMapping("/bloqueios")
public class BloqueioController {

    private final BloqueioService bloqueioService;

    public BloqueioController(BloqueioService bloqueioService) {
        this.bloqueioService = bloqueioService;
    }

    @Operation(
            summary = "Criação de bloqueio",
            description = "Criar um bloqueio para o periodo requisitado via Corpo de requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bloqueio criado",
                    content = @Content(schema = @Schema(implementation = BloqueioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados ou período inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito com bloqueio ou agendamento existente", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BloqueioResponse> criar(@Valid @RequestBody BloqueioRequest request) {
        BloqueioResponse response = bloqueioService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "Listagem de bloqueios por período",
            description = "Listar bloqueios para um período específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueios do período",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BloqueioResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parâmetros ou período inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<BloqueioResponse>> listarPorPeriodo(
            @Parameter(description = "Identificador do usuário/barbeiro", example = "1", required = true)
            @RequestParam @NotNull Integer barbeiroId,
            @Parameter(description = "Início do período no formato yyyy-MM-dd'T'HH:mm:ss", example = "2026-10-23T00:00:00", required = true)
            @RequestParam @NotNull LocalDateTime inicio,
            @Parameter(description = "Fim do período no formato yyyy-MM-dd'T'HH:mm:ss", example = "2026-10-23T23:59:59", required = true)
            @RequestParam @NotNull LocalDateTime fim
    ) {
        return ResponseEntity.ok(bloqueioService.listarPorPeriodo(barbeiroId, inicio, fim));
    }

    @Operation(
            summary = "Remoção de bloqueio",
            description = "Remover um bloqueio específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bloqueio removido"),
            @ApiResponse(responseCode = "404", description = "Bloqueio não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "Identificador do bloqueio", example = "1")
            @PathVariable Integer id) {
        bloqueioService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
