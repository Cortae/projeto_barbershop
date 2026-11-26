package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.pagamento.PendenteResponse;
import school.sptech.apl_api_barbershop.service.PagamentoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class PagamentoControllerTest {
    @Test
    void deveListarPendentes() {
        // Arrange
        var service = mock(PagamentoService.class);
        var controller = new PagamentoController(service);
        when(service.listarPendentes()).thenReturn(List.of(mock(PendenteResponse.class)));
        // Act
        var resposta = controller.listarPendentes();
        // Assert
        assertEquals(1, resposta.getBody().size());
        then(service).should().listarPendentes();
    }

    @Test
    void devePropagarErroDoService() {
        // Arrange
        var service = mock(PagamentoService.class);
        var controller = new PagamentoController(service);
        when(service.listarPendentes()).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, controller::listarPendentes);
        then(service).should().listarPendentes();
    }
}
