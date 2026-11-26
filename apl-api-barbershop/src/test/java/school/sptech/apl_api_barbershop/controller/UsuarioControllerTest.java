package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.*;
import school.sptech.apl_api_barbershop.dto.*;
import school.sptech.apl_api_barbershop.service.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @AfterEach
    void limpar() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void deveCadastrarEBuscar() {
        // Arrange
        var http = new MockHttpServletRequest();
        http.setRequestURI("/usuarios");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(http));
        var service = mock(UsuarioService.class);
        var controller = new UsuarioController(service);
        var request = mock(UsuarioRequest.class);
        var response = new UsuarioResponse(1, "Ana", "a@b.com", "ADMIN");
        when(service.cadastrar(request)).thenReturn(response);
        when(service.buscarPorId(1)).thenReturn(response);
        // Act
        var criado = controller.cadastrar(request);
        var encontrado = controller.buscarPorId(1);
        // Assert
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertSame(response, encontrado.getBody());
        then(service).should().cadastrar(request);
        then(service).should().buscarPorId(1);
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(UsuarioService.class);
        var controller = new UsuarioController(service);
        var request = mock(UsuarioRequest.class);
        when(service.cadastrar(request)).thenThrow(new RuntimeException("falha"));
        when(service.buscarPorId(9)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.cadastrar(request));
        assertThrows(RuntimeException.class, () -> controller.buscarPorId(9));
    }
}
