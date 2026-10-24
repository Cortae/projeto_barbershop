
package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import school.sptech.apl_api_barbershop.dto.AtivoRequest;
import school.sptech.apl_api_barbershop.dto.ServicoRequest;
import school.sptech.apl_api_barbershop.dto.ServicoResponse;
import school.sptech.apl_api_barbershop.service.ServicoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> editar(
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

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listar(
            @RequestParam(required = false) Boolean ativo) {

        List<ServicoResponse> servicos = service.listar(ativo);

        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscar(
            @PathVariable Integer id) {

        ServicoResponse servico = service.buscar(id);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<ServicoResponse> alterarAtivo(
            @PathVariable Integer id,
            @Valid @RequestBody AtivoRequest request) {

        ServicoResponse servico = service.alterarAtivo(id, request.ativo());

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }
}

