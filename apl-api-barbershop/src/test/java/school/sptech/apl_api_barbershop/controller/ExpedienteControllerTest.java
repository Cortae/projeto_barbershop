package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.expediente.*;
import school.sptech.apl_api_barbershop.service.ExpedienteService;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ExpedienteControllerTest {
    @Test
    void deveConfigurarEConsultar() {
        // Arrange
        var service = mock(ExpedienteService.class);
        var controller = new ExpedienteController(service);
        var request = mock(ExpedienteRequest.class);
        var response = mock(ExpedienteResponse.class);
        when(service.configurar(1, request)).thenReturn(response);
        when(service.consultar(1)).thenReturn(response);
        // Act
        var configurado = controller.configurar(1, request);
        var consultado = controller.consultar(1);
        // Assert
        assertSame(response, configurado.getBody());
        assertSame(response, consultado.getBody());
        then(service).should().configurar(1, request);
        then(service).should().consultar(1);
    }

    @Test
    void endpointsDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(ExpedienteService.class);
        var controller = new ExpedienteController(service);
        var request = mock(ExpedienteRequest.class);
        when(service.configurar(9, request)).thenThrow(new RuntimeException("falha"));
        when(service.consultar(9)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.configurar(9, request));
        assertThrows(RuntimeException.class, () -> controller.consultar(9));
    }
}
