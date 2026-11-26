package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.entity.StatusAgendamento;
import school.sptech.apl_api_barbershop.repository.StatusAgendamentoRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class StatusAgendamentoServiceTest {
    @Test
    void existeStatusContainingDeveRetornarTrue() {
        // Arrange
        var repository = mock(StatusAgendamentoRepository.class);
        var service = new StatusAgendamentoService(repository);
        when(repository.existsByTituloContainingIgnoreCase("AGENDADO")).thenReturn(true);
        // Act
        var existe = service.existeStatusContaining("AGENDADO");
        // Assert
        assertTrue(existe);
        then(repository).should().existsByTituloContainingIgnoreCase("AGENDADO");
    }

    @Test
    void buscaStatusDeveRetornarStatus() {
        // Arrange
        var repository = mock(StatusAgendamentoRepository.class);
        var service = new StatusAgendamentoService(repository);
        var status = new StatusAgendamento(1, "AGENDADO");
        when(repository.findByTituloContainingIgnoreCase("AGENDADO")).thenReturn(status);
        // Act
        var encontrado = service.buscaStatus("AGENDADO");
        // Assert
        assertSame(status, encontrado);
        then(repository).should().findByTituloContainingIgnoreCase("AGENDADO");
    }

    @Test
    void existeStatusContainingDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(StatusAgendamentoRepository.class);
        var service = new StatusAgendamentoService(repository);
        when(repository.existsByTituloContainingIgnoreCase("X")).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.existeStatusContaining("X"));
        then(repository).should().existsByTituloContainingIgnoreCase("X");
    }

    @Test
    void buscaStatusDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(StatusAgendamentoRepository.class);
        var service = new StatusAgendamentoService(repository);
        when(repository.findByTituloContainingIgnoreCase("X")).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.buscaStatus("X"));
        then(repository).should().findByTituloContainingIgnoreCase("X");
    }
}
