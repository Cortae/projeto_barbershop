package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import school.sptech.apl_api_barbershop.dto.cliente.*;
import school.sptech.apl_api_barbershop.service.ClienteService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ClienteControllerTest {
    @Test
    void deveExecutarCrudEHistorico() {
        // Arrange
        var service = mock(ClienteService.class);
        var controller = new ClienteController(service);
        var request = mock(ClienteRequest.class);
        var response = mock(ClienteResponse.class);
        when(service.cadastrar(request)).thenReturn(response);
        when(service.editar(1, request)).thenReturn(response);
        when(service.buscar(1)).thenReturn(response);
        when(service.listar(null)).thenReturn(List.of());
        when(service.historico(1)).thenReturn(List.of());
        // Act
        var criado = controller.cadastrar(request);
        var editado = controller.editar(1, request);
        var encontrado = controller.buscar(1);
        var lista = controller.listar(null);
        var historico = controller.historico(1);
        var excluido = controller.excluir(1);
        // Assert
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertSame(response, editado.getBody());
        assertSame(response, encontrado.getBody());
        assertTrue(lista.getBody().isEmpty());
        assertTrue(historico.getBody().isEmpty());
        assertEquals(HttpStatus.NO_CONTENT, excluido.getStatusCode());
        then(service).should().excluir(1);
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(ClienteService.class);
        var controller = new ClienteController(service);
        var request = mock(ClienteRequest.class);
        when(service.cadastrar(request)).thenThrow(new RuntimeException("falha"));
        when(service.editar(9, request)).thenThrow(new RuntimeException("falha"));
        when(service.buscar(9)).thenThrow(new RuntimeException("falha"));
        when(service.listar("erro")).thenThrow(new RuntimeException("falha"));
        when(service.historico(9)).thenThrow(new RuntimeException("falha"));
        doThrow(new RuntimeException("falha")).when(service).excluir(9);
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.cadastrar(request));
        assertThrows(RuntimeException.class, () -> controller.editar(9, request));
        assertThrows(RuntimeException.class, () -> controller.buscar(9));
        assertThrows(RuntimeException.class, () -> controller.listar("erro"));
        assertThrows(RuntimeException.class, () -> controller.historico(9));
        assertThrows(RuntimeException.class, () -> controller.excluir(9));
    }
}
