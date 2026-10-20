
package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.LoginRequest;
import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.service.UsuarioService;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    public AutenticacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(
            @Valid @RequestBody LoginRequest request) {

        UsuarioResponse response = usuarioService.autenticar(request);

        return ResponseEntity.ok(response);
    }
}
