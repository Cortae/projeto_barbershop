package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaMesResponse;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaResponse;
import school.sptech.apl_api_barbershop.service.DespesaService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Tag(name = "Despesas", description = "Cadastro, consulta mensal, edição e exclusão de despesas.")
@RestController
@RequestMapping("/despesas")
public class DespesaController {
    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @Operation(summary = "Cadastrar despesa", description = "Registra uma despesa vinculada a um usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Despesa cadastrada",
                    content = @Content(schema = @Schema(implementation = DespesaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Falha de validação dos campos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DespesaResponse> cadastrar(@RequestBody @Valid DespesaRequest request){
        return ResponseEntity.status(201).body(despesaService.cadastrar(request));
    }

    @Operation(summary = "Listar despesas do mês", description = "Devolve as despesas do mês informado e o total do mês.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesas do mês",
                    content = @Content(schema = @Schema(implementation = DespesaMesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Mês ausente ou em formato inválido", content = @Content)
    })
    @GetMapping
    public ResponseEntity<DespesaMesResponse> listarPorMes(@Parameter(description = "Mês no formato AAAA-MM", example = "2026-10", required = true) @RequestParam YearMonth mes) {
        return ResponseEntity.ok(despesaService.listarPorMes(mes));
    }

    @Operation(summary = "Editar despesa", description = "Substitui os dados de uma despesa existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Despesa atualizada",
                    content = @Content(schema = @Schema(implementation = DespesaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Falha de validação dos campos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Despesa ou usuário não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponse> editar(@Parameter(description = "Id da despesa", example = "1") @PathVariable Integer id, @RequestBody @Valid DespesaRequest request) {
        return ResponseEntity.ok(despesaService.editar(id, request));
    }

    @Operation(summary = "Excluir despesa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Despesa excluída"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "Id da despesa", example = "4") @PathVariable Integer id) {
        despesaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
