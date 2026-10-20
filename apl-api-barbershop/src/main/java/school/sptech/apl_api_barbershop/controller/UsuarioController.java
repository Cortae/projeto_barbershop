
package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import school.sptech.apl_api_barbershop.dto.UsuarioRequest;
import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.service.UsuarioService;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse response = usuarioService.cadastrar(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable Integer id) {

        UsuarioResponse response = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }
}
