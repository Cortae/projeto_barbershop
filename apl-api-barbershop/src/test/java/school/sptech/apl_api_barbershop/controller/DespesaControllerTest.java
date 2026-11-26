package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import school.sptech.apl_api_barbershop.dto.despesa.*;
import school.sptech.apl_api_barbershop.service.DespesaService;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class DespesaControllerTest {
    @Test
    void deveDelegarCrud() {
        // Arrange
        var service = mock(DespesaService.class);
        var controller = new DespesaController(service);
        var request = mock(DespesaRequest.class);
        var response = mock(DespesaResponse.class);
        var mes = mock(DespesaMesResponse.class);
        when(service.cadastrar(request)).thenReturn(response);
        when(service.editar(1, request)).thenReturn(response);
        when(service.listarPorMes(YearMonth.of(2026, 1))).thenReturn(mes);
        // Act
        var criado = controller.cadastrar(request);
        var listado = controller.listarPorMes(YearMonth.of(2026, 1));
        var editado = controller.editar(1, request);
        var excluido = controller.excluir(1);
        // Assert
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertSame(mes, listado.getBody());
        assertSame(response, editado.getBody());
        assertEquals(HttpStatus.NO_CONTENT, excluido.getStatusCode());
        then(service).should().excluir(1);
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(DespesaService.class);
        var controller = new DespesaController(service);
        var request = mock(DespesaRequest.class);
        var mes = YearMonth.of(2026, 1);
        when(service.cadastrar(request)).thenThrow(new RuntimeException("falha"));
        when(service.listarPorMes(mes)).thenThrow(new RuntimeException("falha"));
        when(service.editar(9, request)).thenThrow(new RuntimeException("falha"));
        doThrow(new RuntimeException("falha")).when(service).excluir(9);
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.cadastrar(request));
        assertThrows(RuntimeException.class, () -> controller.listarPorMes(mes));
        assertThrows(RuntimeException.class, () -> controller.editar(9, request));
        assertThrows(RuntimeException.class, () -> controller.excluir(9));
    }
}
