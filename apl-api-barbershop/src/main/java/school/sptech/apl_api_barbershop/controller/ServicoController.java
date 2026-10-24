

package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import school.sptech.apl_api_barbershop.dto.servico.AtivoRequest;
import school.sptech.apl_api_barbershop.dto.servico.ServicoRequest;
import school.sptech.apl_api_barbershop.dto.servico.ServicoResponse;
import school.sptech.apl_api_barbershop.service.ServicoService;

import java.net.URI;
import java.util.List;

@Tag(name = "Serviços", description = "Operações sobre serviços.")
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    // CADASTRAR SERVIÇO
    @Operation(
            summary = "Cadastrar serviço",
            description = "Endpoint responsável por cadastrar um novo serviço."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Serviço cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do serviço inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Já existe um serviço com esse título",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<ServicoResponse> cadastrar(
            @Valid @RequestBody ServicoRequest request) {

        try {
            ServicoResponse servico = service.cadastrar(request);

            URI uri = URI.create("/servicos/" + servico.id());

            return ResponseEntity.created(uri).body(servico);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    // EDITAR SERVIÇO
    @Operation(
            summary = "Editar serviço",
            description = "Endpoint responsável por atualizar os dados de um serviço pelo ID informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do serviço inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito com título de outro serviço",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> editar(
            @Parameter(description = "Identificador do serviço", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ServicoRequest request) {

        try {
            ServicoResponse servico = service.editar(id, request);

            if (servico == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(servico);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }

    // LISTAR SERVIÇOS
    @Operation(
            summary = "Listar serviços",
            description = "Endpoint responsável por listar os serviços cadastrados, permitindo filtrar pelo status ativo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviços listados com sucesso"
            )
    })
    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listar(
            @Parameter(description = "Filtrar serviços ativos ou inativos", example = "true")
            @RequestParam(required = false) Boolean ativo) {

        List<ServicoResponse> servicos = service.listar(ativo);

        return ResponseEntity.ok(servicos);
    }

    // BUSCAR SERVIÇO POR ID
    @Operation(
            summary = "Buscar serviço por ID",
            description = "Endpoint responsável por buscar um serviço específico pelo ID informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscar(
            @Parameter(description = "Identificador do serviço", example = "1")
            @PathVariable Integer id) {

        ServicoResponse servico = service.buscar(id);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }

    // ALTERAR STATUS DO SERVIÇO
    @Operation(
            summary = "Alterar status do serviço",
            description = "Endpoint responsável por ativar ou desativar um serviço cadastrado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status do serviço atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            )
    })
    @PatchMapping("/{id}/ativo")
    public ResponseEntity<ServicoResponse> alterarAtivo(
            @Parameter(description = "Identificador do serviço", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody AtivoRequest request) {

        ServicoResponse servico = service.alterarAtivo(id, request.ativo());

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }
}
