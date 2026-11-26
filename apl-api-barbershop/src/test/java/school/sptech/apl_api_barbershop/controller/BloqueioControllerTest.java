package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.*;
import school.sptech.apl_api_barbershop.dto.bloqueio.*;
import school.sptech.apl_api_barbershop.service.BloqueioService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class BloqueioControllerTest {
    @AfterEach
    void limpar() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void deveCriarListarERemover() {
        // Arrange
        var http = new MockHttpServletRequest();
        http.setRequestURI("/bloqueios");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(http));
        var service = mock(BloqueioService.class);
        var controller = new BloqueioController(service);
        var request = mock(BloqueioRequest.class);
        var response = new BloqueioResponse();
        response.setId(1);
        var inicio = LocalDateTime.now();
        var fim = inicio.plusHours(1);
        when(service.criar(request)).thenReturn(response);
        when(service.listarPorPeriodo(1, inicio, fim)).thenReturn(List.of(response));
        // Act
        var criado = controller.criar(request);
        var lista = controller.listarPorPeriodo(1, inicio, fim);
        var removido = controller.remover(1);
        // Assert
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertEquals(1, lista.getBody().size());
        assertEquals(HttpStatus.NO_CONTENT, removido.getStatusCode());
        then(service).should().remover(1);
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(BloqueioService.class);
        var controller = new BloqueioController(service);
        var request = mock(BloqueioRequest.class);
        var inicio = LocalDateTime.now();
        var fim = inicio.plusHours(1);
        when(service.criar(request)).thenThrow(new RuntimeException("falha"));
        when(service.listarPorPeriodo(9, inicio, fim)).thenThrow(new RuntimeException("falha"));
        doThrow(new RuntimeException("falha")).when(service).remover(9);
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.criar(request));
        assertThrows(RuntimeException.class, () -> controller.listarPorPeriodo(9, inicio, fim));
        assertThrows(RuntimeException.class, () -> controller.remover(9));
    }
}
