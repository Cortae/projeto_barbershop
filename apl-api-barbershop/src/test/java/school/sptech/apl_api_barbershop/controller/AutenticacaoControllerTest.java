package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import school.sptech.apl_api_barbershop.dto.LoginRequest;
import school.sptech.apl_api_barbershop.dto.RecuperarSenhaRequestDto;
import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.service.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AutenticacaoControllerTest {
    @Test
    void deveAutenticarERecuperarSenha() {
        // Arrange
        UsuarioService service = mock(UsuarioService.class);
        var controller = new AutenticacaoController(service);
        LoginRequest login = mock(LoginRequest.class);
        RecuperarSenhaRequestDto recuperar = mock(RecuperarSenhaRequestDto.class);
        UsuarioResponse usuario = new UsuarioResponse();
        when(service.autenticar(login)).thenReturn(usuario);
        when(recuperar.getEmail()).thenReturn("a@b.com");
        // Act
        var autenticado = controller.login(login);
        var recuperado = controller.recuperarSenha(recuperar);
        // Assert
        assertSame(usuario, autenticado.getBody());
        assertEquals(HttpStatus.OK, recuperado.getStatusCode());
        then(service).should().enviarNovaSenha("a@b.com");
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(UsuarioService.class);
        var controller = new AutenticacaoController(service);
        var login = mock(LoginRequest.class);
        var recuperar = mock(RecuperarSenhaRequestDto.class);
        when(recuperar.getEmail()).thenReturn("x@y.com");
        when(service.autenticar(login)).thenThrow(new RuntimeException("falha"));
        doThrow(new RuntimeException("falha")).when(service).enviarNovaSenha("x@y.com");
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.login(login));
        assertThrows(RuntimeException.class, () -> controller.recuperarSenha(recuperar));
        then(service).should().autenticar(login);
        then(service).should().enviarNovaSenha("x@y.com");
    }
}
