package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.bloqueio.ImportacaoFeriadosResponse;
import school.sptech.apl_api_barbershop.service.BloqueioFeriadoService;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class BloqueioFeriadoControllerTest {
    @Test
    void deveImportarFeriados() {
        // Arrange
        var service = mock(BloqueioFeriadoService.class);
        var controller = new BloqueioFeriadoController(service);
        var esperado = new ImportacaoFeriadosResponse();
        when(service.importar(2026)).thenReturn(esperado);
        // Act
        var resposta = controller.importar(2026);
        // Assert
        assertSame(esperado, resposta.getBody());
        then(service).should().importar(2026);
    }

    @Test
    void devePropagarErroDaImportacao() {
        // Arrange
        var service = mock(BloqueioFeriadoService.class);
        var controller = new BloqueioFeriadoController(service);
        when(service.importar(1899)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> controller.importar(1899));
        then(service).should().importar(1899);
    }
}
