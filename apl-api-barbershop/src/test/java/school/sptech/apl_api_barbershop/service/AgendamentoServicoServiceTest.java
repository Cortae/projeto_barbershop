package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.AgendamentoServicoRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AgendamentoServicoServiceTest {
    @Test
    void vincularDeveSalvarVinculo() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        var agendamento = new Agendamento();
        var servico = new Servico();
        servico.setValorAtual(BigDecimal.TEN);
        servico.setDuracaoAtual(30);
        // Act
        service.vincularAgendamentoServico(agendamento, List.of(servico));
        // Assert
        then(repository).should().save(any(AgendamentoServico.class));
    }

    @Test
    void atualizarDeveSubstituirVinculos() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        var agendamento = new Agendamento();
        agendamento.setIdAgendamento(1);
        var servico = new Servico();
        servico.setValorAtual(BigDecimal.TEN);
        servico.setDuracaoAtual(30);
        when(repository.existsByAgendamentoIdAgendamento(1)).thenReturn(true);
        // Act
        service.atualizarServicos(agendamento, List.of(servico));
        // Assert
        then(repository).should().deleteByAgendamentoIdAgendamento(1);
        then(repository).should().flush();
        then(repository).should().save(any(AgendamentoServico.class));
    }

    @Test
    void buscarDeveRetornarVinculos() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        when(repository.findAllByAgendamentoIdAgendamentoIn(List.of(1))).thenReturn(List.of(new AgendamentoServico()));
        // Act
        var lista = service.buscarPorAgendamentos(List.of(1));
        // Assert
        assertEquals(1, lista.size());
        then(repository).should().findAllByAgendamentoIdAgendamentoIn(List.of(1));
    }

    @Test
    void vincularDevePropagarErroAoSalvar() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        var agendamento = new Agendamento();
        var servico = new Servico();
        when(repository.save(any())).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.vincularAgendamentoServico(agendamento, List.of(servico)));
        then(repository).should().save(any());
    }

    @Test
    void atualizarDevePropagarErroAoExcluir() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        var agendamento = new Agendamento();
        agendamento.setIdAgendamento(1);
        when(repository.existsByAgendamentoIdAgendamento(1)).thenReturn(true);
        doThrow(new RuntimeException("falha")).when(repository).deleteByAgendamentoIdAgendamento(1);
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.atualizarServicos(agendamento, List.of()));
        then(repository).should().deleteByAgendamentoIdAgendamento(1);
    }

    @Test
    void buscarDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(AgendamentoServicoRepository.class);
        var service = new AgendamentoServicoService(repository);
        when(repository.findAllByAgendamentoIdAgendamentoIn(List.of(1))).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.buscarPorAgendamentos(List.of(1)));
        then(repository).should().findAllByAgendamentoIdAgendamentoIn(List.of(1));
    }
}
