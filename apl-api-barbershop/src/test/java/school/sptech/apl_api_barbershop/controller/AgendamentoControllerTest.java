package school.sptech.apl_api_barbershop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import school.sptech.apl_api_barbershop.dto.agendamento.*;
import school.sptech.apl_api_barbershop.service.AgendamentoService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AgendamentoControllerTest {
    @Test
    void deveDelegarOperacoes() {
        // Arrange
        var service = mock(AgendamentoService.class);
        var controller = new AgendamentoController(service);
        var response = mock(AgendamentoResponse.class);
        when(service.buscarPorBarbeiro(1)).thenReturn(List.of(response));
        when(service.atualizarStatus(anyInt(), anyString())).thenReturn(response);
        when(service.buscarPorId(1)).thenReturn(Optional.of(response));
        // Act
        var lista = controller.buscarPorBarbeiro(1);
        var cancelado = controller.cancelarAgendamento(1);
        var encontrado = controller.buscarPorId(1);
        // Assert
        assertEquals(1, lista.getBody().size());
        assertSame(response, cancelado.getBody());
        assertSame(response, encontrado.getBody());
        then(service).should().atualizarStatus(1, "CANCELADO");
    }

    @Test
    void deveDelegarDemaisOperacoes() {
        // Arrange
        var service = mock(AgendamentoService.class);
        var controller = new AgendamentoController(service);
        var response = mock(AgendamentoResponse.class);
        var cadastro = mock(AgendamentoCadastroRequestDto.class);
        var edicao = mock(EditarAgendamentoRequest.class);
        var atualizar = mock(school.sptech.apl_api_barbershop.dto.servico.AtualizarServicosAgendamentoRequestDto.class);
        var remarcar = mock(RemarcarAgendamentoRequestDto.class);
        var pagar = mock(school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest.class);
        var concluir = mock(school.sptech.apl_api_barbershop.dto.pagamento.ConcluirRequest.class);
        when(concluir.getFormaPagamentoId()).thenReturn(2);
        when(service.atualizarStatus(1, "CONFIRMADO")).thenReturn(response);
        when(service.concluir(1, 2)).thenReturn(response);
        when(service.editar(1, edicao)).thenReturn(response);
        when(service.atualizarServicosAgendamento(atualizar)).thenReturn(response);
        when(service.remarcarAgendamento(remarcar)).thenReturn(response);
        when(service.listarPorPeriodo(any(), any())).thenReturn(List.of(response));
        when(service.existeCliente(1)).thenReturn(true);
        when(service.buscarAgendamentosPorCliente(1)).thenReturn(List.of(response));
        when(service.cadastrar(cadastro)).thenReturn(response);
        when(service.registrarPagamento(pagar)).thenReturn(response);
        // Act
        var confirmado = controller.confirmarAgendamento(1, "CONFIRMADO");
        var concluido = controller.concluirAgendamento(concluir, 1);
        var editado = controller.editar(1, edicao);
        var atualizado = controller.atualizarServicosAgendamento(atualizar);
        var remarcado = controller.remarcarAgendamento(remarcar);
        var periodo = controller.listarPorPeriodo(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));
        var cliente = controller.buscarAgendamentosPorCliente(1);
        var criado = controller.cadastrar(cadastro);
        var pago = controller.marcarComoPago(pagar);
        // Assert
        assertSame(response, confirmado.getBody());
        assertSame(response, concluido.getBody());
        assertSame(response, editado.getBody());
        assertSame(response, atualizado.getBody());
        assertSame(response, remarcado.getBody());
        assertEquals(1, periodo.getBody().size());
        assertEquals(1, cliente.getBody().size());
        assertEquals(HttpStatus.CREATED, criado.getStatusCode());
        assertSame(response, pago.getBody());
        then(service).should().concluir(1, 2);
    }

    @Test
    void deveRetornarBadRequestENotFound() {
        // Arrange
        var service = mock(AgendamentoService.class);
        var controller = new AgendamentoController(service);
        when(service.buscarPorId(9)).thenReturn(Optional.empty());
        // Act
        var periodo = controller.listarPorPeriodo(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1));
        var busca = controller.buscarPorId(9);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, periodo.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, busca.getStatusCode());
        then(service).should(never()).listarPorPeriodo(any(), any());
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExiste() {
        // Arrange
        var service = mock(AgendamentoService.class);
        var controller = new AgendamentoController(service);
        when(service.existeCliente(9)).thenReturn(false);
        // Act
        var resposta = controller.buscarAgendamentosPorCliente(9);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        then(service).should(never()).buscarAgendamentosPorCliente(9);
    }

    @Test
    void endpointsDelegadosDevemPropagarErrosDoService() {
        // Arrange
        var service = mock(AgendamentoService.class);
        var controller = new AgendamentoController(service);
        var cadastro = mock(AgendamentoCadastroRequestDto.class);
        var edicao = mock(EditarAgendamentoRequest.class);
        var atualizar = mock(school.sptech.apl_api_barbershop.dto.servico.AtualizarServicosAgendamentoRequestDto.class);
        var remarcar = mock(RemarcarAgendamentoRequestDto.class);
        var pagar = mock(school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest.class);
        var erro = new RuntimeException("falha");
        when(service.buscarPorBarbeiro(9)).thenThrow(erro);
        when(service.atualizarStatus(9, "CANCELADO")).thenThrow(erro);
        when(service.atualizarStatus(9, "CONFIRMADO")).thenThrow(erro);
        when(service.concluir(9, null)).thenThrow(erro);
        when(service.editar(9, edicao)).thenThrow(erro);
        when(service.atualizarServicosAgendamento(atualizar)).thenThrow(erro);
        when(service.remarcarAgendamento(remarcar)).thenThrow(erro);
        when(service.cadastrar(cadastro)).thenThrow(erro);
        when(service.registrarPagamento(pagar)).thenThrow(erro);
        // Act / Assert
        assertThrows(RuntimeException.class, () -> controller.buscarPorBarbeiro(9));
        assertThrows(RuntimeException.class, () -> controller.cancelarAgendamento(9));
        assertThrows(RuntimeException.class, () -> controller.confirmarAgendamento(9, "CONFIRMADO"));
        assertThrows(RuntimeException.class, () -> controller.concluirAgendamento(null, 9));
        assertThrows(RuntimeException.class, () -> controller.editar(9, edicao));
        assertThrows(RuntimeException.class, () -> controller.atualizarServicosAgendamento(atualizar));
        assertThrows(RuntimeException.class, () -> controller.remarcarAgendamento(remarcar));
        assertThrows(RuntimeException.class, () -> controller.cadastrar(cadastro));
        assertThrows(RuntimeException.class, () -> controller.marcarComoPago(pagar));
    }
}
