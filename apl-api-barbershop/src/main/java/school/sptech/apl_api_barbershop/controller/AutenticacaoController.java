
package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.LoginRequest;
import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.service.UsuarioService;

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Autenticação", description = "Operações de login")
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    public AutenticacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário com e-mail e senha."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "E-mail ou senha incorretos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão para acessar o painel",
                    content = @Content
            )
    })
    public ResponseEntity<UsuarioResponse> login(
            @Valid @RequestBody LoginRequest request) {

        UsuarioResponse response = usuarioService.autenticar(request);

        return ResponseEntity.ok(response);
    }
}
