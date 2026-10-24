
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

import school.sptech.apl_api_barbershop.dto.cliente.ClienteHistoricoResponse;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteRequest;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteResponse;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteItemResponse;
import school.sptech.apl_api_barbershop.service.ClienteService;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Operações sobre clientes.")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // CADASTRAR CLIENTE
    @Operation(
            summary = "Cadastrar cliente",
            description = "Endpoint responsável por cadastrar um novo cliente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do cliente inválidos",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse cliente = service.cadastrar(request);

        URI uri = URI.create("/clientes/" + cliente.id());

        return ResponseEntity.created(uri).body(cliente);
    }

    // EDITAR CLIENTE
    @Operation(
            summary = "Editar cliente",
            description = "Endpoint responsável por atualizar os dados de um cliente pelo ID informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do cliente inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> editar(
            @Parameter(description = "Identificador do cliente", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse cliente = service.editar(id, request);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    // LISTAR CLIENTES
    @Operation(
            summary = "Listar clientes",
            description = "Endpoint responsável por listar os clientes cadastrados, permitindo filtrar por nome."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clientes listados com sucesso"
            )
    })
    @GetMapping
    public ResponseEntity<List<ClienteItemResponse>> listar(
            @Parameter(description = "Nome utilizado para filtrar clientes", example = "João")
            @RequestParam(required = false) String busca) {

        List<ClienteItemResponse> clientes = service.listar(busca);

        return ResponseEntity.ok(clientes);
    }

    // BUSCAR CLIENTE POR ID
    @Operation(
            summary = "Buscar cliente por ID",
            description = "Endpoint responsável por buscar um cliente específico pelo ID informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(
            @Parameter(description = "Identificador do cliente", example = "1")
            @PathVariable Integer id) {

        ClienteResponse cliente = service.buscar(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    // CONSULTAR HISTÓRICO DO CLIENTE
    @Operation(
            summary = "Consultar histórico do cliente",
            description = "Endpoint responsável por consultar o histórico de agendamentos de um cliente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico consultado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}/historico")
    public ResponseEntity<List<ClienteHistoricoResponse>> historico(
            @Parameter(description = "Identificador do cliente", example = "1")
            @PathVariable Integer id) {

        List<ClienteHistoricoResponse> historico = service.historico(id);

        if (historico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(historico);
    }
}
