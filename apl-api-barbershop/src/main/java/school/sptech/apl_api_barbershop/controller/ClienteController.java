
package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import school.sptech.apl_api_barbershop.dto.ClienteHistoricoResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import school.sptech.apl_api_barbershop.dto.ClienteRequest;
import school.sptech.apl_api_barbershop.dto.ClienteResponse;
import school.sptech.apl_api_barbershop.dto.ClienteItemResponse;
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

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse cliente = service.cadastrar(request);

        URI uri = URI.create("/clientes/" + cliente.id());

        return ResponseEntity.created(uri).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> editar(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse cliente = service.editar(id, request);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteItemResponse>> listar(
            @RequestParam(required = false) String busca) {

        List<ClienteItemResponse> clientes = service.listar(busca);

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(
            @PathVariable Integer id) {

        ClienteResponse cliente = service.buscar(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<ClienteHistoricoResponse>> historico(
            @PathVariable Integer id) {

        List<ClienteHistoricoResponse> historico =
                service.historico(id);

        if (historico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(historico);
    }
}
