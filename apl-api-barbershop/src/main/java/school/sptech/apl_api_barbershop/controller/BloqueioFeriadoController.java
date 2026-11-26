package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.apl_api_barbershop.dto.bloqueio.ImportacaoFeriadosResponse;
import school.sptech.apl_api_barbershop.service.BloqueioFeriadoService;

@Tag(name = "Bloqueios", description = "Operações sobre bloqueios.")
@RestController
@RequestMapping("/bloqueios/feriados")
public class BloqueioFeriadoController {

    private final BloqueioFeriadoService bloqueioFeriadoService;

    public BloqueioFeriadoController(BloqueioFeriadoService bloqueioFeriadoService) {
        this.bloqueioFeriadoService = bloqueioFeriadoService;
    }

    @Operation(
            summary = "Importação de feriados nacionais",
            description = "Consulta os feriados nacionais na BrasilAPI e cria bloqueios de dia inteiro para os barbeiros."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriados processados",
                    content = @Content(schema = @Schema(implementation = ImportacaoFeriadosResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ano fora do intervalo permitido", content = @Content),
            @ApiResponse(responseCode = "502", description = "BrasilAPI indisponível ou com resposta inválida",
                    content = @Content)
    })
    @PostMapping("/{ano}")
    public ResponseEntity<ImportacaoFeriadosResponse> importar(
            @Parameter(description = "Ano dos feriados, entre 1900 e 2199", example = "2026")
            @PathVariable int ano
    ) {
        return ResponseEntity.ok(bloqueioFeriadoService.importar(ano));
    }
}
