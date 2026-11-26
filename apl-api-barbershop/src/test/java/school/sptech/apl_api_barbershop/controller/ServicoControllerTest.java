package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import school.sptech.apl_api_barbershop.dto.servico.*;
import school.sptech.apl_api_barbershop.service.ServicoService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ServicoControllerTest {
    @Test
    void deveExecutarCrud() {
        // Arrange
        var service = mock(ServicoService.class);
        var controller = new ServicoController(service);
        var request = new ServicoRequest("Corte", BigDecimal.TEN, 30);
        var response = new ServicoResponse(1, "Corte", BigDecimal.TEN, 30, true);
        when(service.cadastrar(request)).thenReturn(response);
        when(service.editar(1, request)).thenReturn(response);
        when(service.buscar(1)).thenReturn(response);
        when(service.listar(true)).thenReturn(List.of(response));
        when(service.alterarAtivo(1, false)).thenReturn(response);
        // Act
        var criado = controller.cadastrar(request);
        var editado = controller.editar(1, request);
        var encontrado = controller.buscar(1);
        var lista = controller.listar(true);
        var alterado = controller.alterarAtivo(1, new AtivoRequest(false));
        var excluido = controller.excluir(1);
        // Assert
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertSame(response, editado.getBody());
        assertSame(response, encontrado.getBody());
        assertEquals(1, lista.getBody().size());
        assertSame(response, alterado.getBody());
        assertEquals(HttpStatus.NO_CONTENT, excluido.getStatusCode());
        then(service).should().excluir(1);
    }

    @Test
    void deveTraduzirConflitoENaoEncontrado() {
        // Arrange
        var service = mock(ServicoService.class);
        var controller = new ServicoController(service);
        var request = new ServicoRequest("Corte", BigDecimal.TEN, 30);
        when(service.cadastrar(request)).thenThrow(new IllegalArgumentException());
        // Act
        var conflito = controller.cadastrar(request);
        var inexistente = controller.buscar(9);
        // Assert
        assertEquals(HttpStatus.CONFLICT, conflito.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, inexistente.getStatusCode());
    }

    @Test
    void demaisEndpointsDevemTratarOuPropagarErros() {
        // Arrange
        var service = mock(ServicoService.class);
        var controller = new ServicoController(service);
        var request = new ServicoRequest("Corte", BigDecimal.TEN, 30);
        when(service.editar(9, request)).thenReturn(null);
        when(service.listar(false)).thenThrow(new RuntimeException("falha"));
        when(service.alterarAtivo(9, true)).thenReturn(null);
        doThrow(new RuntimeException("falha")).when(service).excluir(9);
        // Act
        var edicao = controller.editar(9, request);
        var alteracao = controller.alterarAtivo(9, new AtivoRequest(true));
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, edicao.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, alteracao.getStatusCode());
        assertThrows(RuntimeException.class, () -> controller.listar(false));
        assertThrows(RuntimeException.class, () -> controller.excluir(9));
    }
}
