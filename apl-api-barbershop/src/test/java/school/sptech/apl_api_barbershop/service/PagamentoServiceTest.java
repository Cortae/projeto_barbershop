package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {
    @Test
    void registrarDeveRetornarPagamentoPendente() {
        // Arrange
        var pagamentos = mock(PagamentoRepository.class);
        var formas = mock(FormaPagamentoRepository.class);
        var agendamentos = mock(AgendamentoRepository.class);
        var service = new PagamentoService(pagamentos, formas, agendamentos);
        var pagamento = new Pagamento();
        pagamento.setId(1);
        pagamento.setValor(BigDecimal.TEN);
        var cliente = new Cliente();
        cliente.setNome("Ana");
        var agendamento = new Agendamento();
        agendamento.setPagamento(pagamento);
        agendamento.setCliente(cliente);
        agendamento.setTitulo("Corte");
        agendamento.setHorarioInicio(LocalDateTime.now());
        when(pagamentos.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pagamentos.findById(1)).thenReturn(Optional.of(pagamento));
        when(formas.findById(1)).thenReturn(Optional.of(new FormaPagamento(1, "PIX")));
        when(agendamentos.buscarPagamentosPendentes()).thenReturn(List.of(agendamento));
        // Act
        var registrado = service.registrar(BigDecimal.TEN);
        // Assert
        assertFalse(registrado.isPago());
        then(pagamentos).should().save(any());
    }

    @Test
    void marcarComoPagoDeveRetornarPagamentoPago() {
        // Arrange
        var pagamentos=mock(PagamentoRepository.class); var formas=mock(FormaPagamentoRepository.class); var service=new PagamentoService(pagamentos,formas,mock(AgendamentoRepository.class)); var pagamento=new Pagamento(); pagamento.setId(1); when(pagamentos.findById(1)).thenReturn(Optional.of(pagamento)); when(formas.findById(1)).thenReturn(Optional.of(new FormaPagamento(1,"PIX"))); when(pagamentos.save(pagamento)).thenReturn(pagamento);
        // Act
        var resposta=service.marcarComoPago(1,1);
        // Assert
        assertTrue(resposta.isPago()); then(pagamentos).should().save(pagamento);
    }

    @Test
    void listarPendentesDeveRetornarLista() {
        // Arrange
        var agendas=mock(AgendamentoRepository.class); var service=new PagamentoService(mock(PagamentoRepository.class),mock(FormaPagamentoRepository.class),agendas); var pagamento=new Pagamento(); pagamento.setValor(BigDecimal.TEN); var cliente=new Cliente(); cliente.setNome("Ana"); var agenda=new Agendamento(); agenda.setPagamento(pagamento); agenda.setCliente(cliente); agenda.setTitulo("Corte"); agenda.setHorarioInicio(LocalDateTime.now()); when(agendas.buscarPagamentosPendentes()).thenReturn(List.of(agenda));
        // Act
        var lista=service.listarPendentes();
        // Assert
        assertEquals(1,lista.size()); then(agendas).should().buscarPagamentosPendentes();
    }

    @Test
    void registrarDevePropagarErroAoSalvar() {
        // Arrange
        var repository = mock(PagamentoRepository.class);
        var service = new PagamentoService(repository, mock(FormaPagamentoRepository.class), mock(AgendamentoRepository.class));
        when(repository.save(any())).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.registrar(BigDecimal.TEN));
    }

    @Test
    void marcarComoPagoDeveFalharQuandoPagamentoNaoExiste() {
        // Arrange
        var repository = mock(PagamentoRepository.class);
        var service = new PagamentoService(repository, mock(FormaPagamentoRepository.class), mock(AgendamentoRepository.class));
        when(repository.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.marcarComoPago(9, 1));
    }

    @Test
    void listarPendentesDevePropagarErroDoRepositorio() {
        // Arrange
        var agendas = mock(AgendamentoRepository.class);
        var service = new PagamentoService(mock(PagamentoRepository.class), mock(FormaPagamentoRepository.class), agendas);
        when(agendas.buscarPagamentosPendentes()).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, service::listarPendentes);
    }
}
