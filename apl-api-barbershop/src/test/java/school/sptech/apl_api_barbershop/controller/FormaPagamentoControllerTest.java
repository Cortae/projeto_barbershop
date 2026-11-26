package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.entity.FormaPagamento;
import school.sptech.apl_api_barbershop.repository.FormaPagamentoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class FormaPagamentoControllerTest {
    @Test
    void deveListar() {
        // Arrange
        var repository = mock(FormaPagamentoRepository.class);
        var controller = new FormaPagamentoController(repository);
        when(repository.findAllByOrderByTituloAsc()).thenReturn(List.of(new FormaPagamento(1, "PIX")));
        // Act
        var resposta = controller.listar();
        // Assert
        assertEquals("PIX", resposta.getFirst().getTitulo());
        then(repository).should().findAllByOrderByTituloAsc();
    }

    @Test
    void devePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(FormaPagamentoRepository.class);
        var controller = new FormaPagamentoController(repository);
        when(repository.findAllByOrderByTituloAsc()).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, controller::listar);
        then(repository).should().findAllByOrderByTituloAsc();
    }
}
