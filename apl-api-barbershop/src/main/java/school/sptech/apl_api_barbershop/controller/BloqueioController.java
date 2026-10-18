package school.sptech.apl_api_barbershop.controller;

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

    @PostMapping
    public ResponseEntity<BloqueioResponse> criar(@Valid @RequestBody BloqueioRequest request) {
        BloqueioResponse response = bloqueioService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BloqueioResponse>> listarPorPeriodo(
            @RequestParam @NotNull Integer barbeiroId,
            @RequestParam @NotNull LocalDateTime inicio,
            @RequestParam @NotNull LocalDateTime fim
    ) {
        return ResponseEntity.ok(bloqueioService.listarPorPeriodo(barbeiroId, inicio, fim));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        bloqueioService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
