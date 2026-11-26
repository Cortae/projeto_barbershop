package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoResponse;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.exception.BadRequestException;
import school.sptech.apl_api_barbershop.exception.BusinessConflictException;
import school.sptech.apl_api_barbershop.exception.NotFoundException;
import school.sptech.apl_api_barbershop.mapper.agendamento.AgendamentoMapper;
import school.sptech.apl_api_barbershop.repository.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AgendamentoServiceTest {
    @Test
    void deveBuscarAtualizarStatusEValidarDisponibilidade() {
        // Arrange
        var agendas = mock(AgendamentoRepository.class);
        var status = mock(StatusAgendamentoService.class);
        var clientes = mock(ClienteRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var vinculos = mock(AgendamentoServicoService.class);
        var mapper = mock(AgendamentoMapper.class);
        var service = new AgendamentoService(agendas, mock(ServicoRepository.class), status, clientes, mock(PagamentoService.class), mock(PagamentoRepository.class), usuarios, mock(BloqueioRepository.class), vinculos, mapper);
        var entidade = new Agendamento();
        entidade.setIdAgendamento(1);
        entidade.setStatusAgendamento(new StatusAgendamento(1, "AGENDADO"));
        var response = mock(AgendamentoResponse.class);
        when(usuarios.existsById(1)).thenReturn(true);
        when(agendas.findAllByBarbeiro_IdOrderByHorarioInicioAsc(1)).thenReturn(List.of(entidade));
        when(agendas.findById(1)).thenReturn(Optional.of(entidade));
        when(status.existeStatusContaining("CONFIRMADO")).thenReturn(true);
        when(status.buscaStatus("CONFIRMADO")).thenReturn(new StatusAgendamento(2, "CONFIRMADO"));
        when(agendas.save(entidade)).thenReturn(entidade);
        when(mapper.toResponse(eq(entidade), anyList())).thenReturn(response);
        when(clientes.existsById(1)).thenReturn(true);
        when(agendas.existsById(1)).thenReturn(true);
        // Act
        var lista = service.buscarPorBarbeiro(1);
        var atualizado = service.atualizarStatus(1, "CONFIRMADO");
        var clienteExiste = service.existeCliente(1);
        var agendaExiste = service.existeAgendamento(1);
        var inicio = LocalDateTime.now();
        service.validarDisponibilidade(inicio, inicio.plusHours(1), 1, null);
        // Assert
        assertEquals(1, lista.size());
        assertSame(response, atualizado);
        assertTrue(clienteExiste);
        assertTrue(agendaExiste);
        then(agendas).should().save(entidade);
    }

    @Test
    void deveConcluirListarBuscarClienteEBuscarPorId() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        when(f.status.buscaStatus("CONCLUIDO")).thenReturn(new StatusAgendamento(2, "CONCLUIDO"));
        when(f.agendas.save(f.agendamento)).thenReturn(f.agendamento);
        when(f.agendas.findByHorarioInicioBetweenOrderByHorarioInicioAsc(any(), any())).thenReturn(List.of(f.agendamento));
        when(f.agendas.findByCliente_IdAndStatusAgendamento_TituloIgnoreCaseOrderByHorarioInicioDesc(1, "CONCLUIDO")).thenReturn(List.of(f.agendamento));
        // Act
        var concluido = f.service.concluir(1, null);
        var periodo = f.service.listarPorPeriodo(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));
        var cliente = f.service.buscarAgendamentosPorCliente(1);
        var porId = f.service.buscarPorId(1);
        var estaConcluido = f.service.agendamentoEstaConcluido(1);
        // Assert
        assertSame(f.response, concluido);
        assertEquals(1, periodo.size());
        assertEquals(1, cliente.size());
        assertTrue(porId.isPresent());
        assertTrue(estaConcluido);
        then(f.agendas).should().save(f.agendamento);
    }

    @Test
    void deveCadastrarEditarAtualizarServicosERemarcar() {
        // Arrange
        var f = new Fixture();
        var inicio = LocalDateTime.of(2026, 1, 2, 10, 0);
        var cadastro = new school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoCadastroRequestDto(1, 1, List.of(1), inicio);
        var edicao = new school.sptech.apl_api_barbershop.dto.agendamento.EditarAgendamentoRequest();
        edicao.setCliente(1);
        edicao.setServicos(List.of(1));
        edicao.setInicio(inicio);
        var atualizar = new school.sptech.apl_api_barbershop.dto.servico.AtualizarServicosAgendamentoRequestDto(1, List.of(1));
        var remarcar = new school.sptech.apl_api_barbershop.dto.agendamento.RemarcarAgendamentoRequestDto(1, inicio.plusDays(1));
        when(f.clientes.findById(1)).thenReturn(Optional.of(f.cliente));
        when(f.usuarios.findById(1)).thenReturn(Optional.of(f.barbeiro));
        when(f.servicos.findAllById(List.of(1))).thenReturn(List.of(f.servico));
        when(f.pagamentos.registrar(BigDecimal.TEN)).thenReturn(f.pagamento);
        when(f.status.buscaStatus("AGENDADO")).thenReturn(new StatusAgendamento(1, "AGENDADO"));
        when(f.agendas.save(any())).thenAnswer(i -> {
            Agendamento a = i.getArgument(0);
            if (a.getIdAgendamento() == null) a.setIdAgendamento(1);
            return a;
        });
        // Act
        var criado = f.service.cadastrar(cadastro);
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        var editado = f.service.editar(1, edicao);
        var atualizado = f.service.atualizarServicosAgendamento(atualizar);
        var remarcado = f.service.remarcarAgendamento(remarcar);
        // Assert
        assertSame(f.response, criado);
        assertSame(f.response, editado);
        assertSame(f.response, atualizado);
        assertSame(f.response, remarcado);
        then(f.vinculos).should().vincularAgendamentoServico(any(), eq(List.of(f.servico)));
        then(f.vinculos).should(atLeastOnce()).atualizarServicos(any(), eq(List.of(f.servico)));
    }

    @Test
    void deveRegistrarPagamentoDeAgendamentoConcluido() {
        // Arrange
        var f = new Fixture();
        f.agendamento.setStatusAgendamento(new StatusAgendamento(2, "CONCLUIDO"));
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        var request = new school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest(1, 2);
        // Act
        var resposta = f.service.registrarPagamento(request);
        // Assert
        assertSame(f.response, resposta);
        then(f.pagamentos).should().marcarComoPago(1, 2);
    }

    @Test
    void buscarPorBarbeiroDeveFalharQuandoUsuarioNaoExiste() {
        // Arrange
        var f = new Fixture();
        when(f.usuarios.existsById(9)).thenReturn(false);
        // Act / Assert
        assertThrows(NotFoundException.class, () -> f.service.buscarPorBarbeiro(9));
    }

    @Test
    void atualizarStatusDeveRejeitarStatusInvalido() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        when(f.status.existeStatusContaining("INVALIDO")).thenReturn(false);
        // Act / Assert
        assertThrows(BadRequestException.class, () -> f.service.atualizarStatus(1, "INVALIDO"));
    }

    @Test
    void concluirDeveRejeitarAgendamentoCancelado() {
        // Arrange
        var f = new Fixture();
        f.agendamento.setStatusAgendamento(new StatusAgendamento(3, "CANCELADO"));
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        // Act / Assert
        assertThrows(BusinessConflictException.class, () -> f.service.concluir(1, null));
    }

    @Test
    void listarPorPeriodoDevePropagarErroDoRepositorio() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findByHorarioInicioBetweenOrderByHorarioInicioAsc(any(), any())).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> f.service.listarPorPeriodo(LocalDate.now(), LocalDate.now()));
    }

    @Test
    void existeClienteDevePropagarErroDoRepositorio() {
        // Arrange
        var f = new Fixture();
        when(f.clientes.existsById(1)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> f.service.existeCliente(1));
    }

    @Test
    void buscarPorClienteDevePropagarErroDoRepositorio() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findByCliente_IdAndStatusAgendamento_TituloIgnoreCaseOrderByHorarioInicioDesc(1, "CONCLUIDO")).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> f.service.buscarAgendamentosPorCliente(1));
    }

    @Test
    void cadastrarDeveFalharQuandoClienteNaoExiste() {
        // Arrange
        var f = new Fixture();
        var request = new school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoCadastroRequestDto(1, 9, List.of(1), LocalDateTime.now());
        when(f.clientes.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(BadRequestException.class, () -> f.service.cadastrar(request));
    }

    @Test
    void editarDeveFalharQuandoAgendamentoNaoExiste() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(NotFoundException.class, () -> f.service.editar(9, new school.sptech.apl_api_barbershop.dto.agendamento.EditarAgendamentoRequest()));
    }

    @Test
    void atualizarServicosDeveFalharQuandoAgendamentoNaoExiste() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(9)).thenReturn(Optional.empty());
        var request = new school.sptech.apl_api_barbershop.dto.servico.AtualizarServicosAgendamentoRequestDto(9, List.of(1));
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> f.service.atualizarServicosAgendamento(request));
    }

    @Test
    void remarcarDeveFalharQuandoAgendamentoNaoExiste() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(9)).thenReturn(Optional.empty());
        var request = new school.sptech.apl_api_barbershop.dto.agendamento.RemarcarAgendamentoRequestDto(9, LocalDateTime.now());
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> f.service.remarcarAgendamento(request));
    }

    @Test
    void validarDisponibilidadeDeveRejeitarConflito() {
        // Arrange
        var f = new Fixture();
        var inicio = LocalDateTime.now();
        var fim = inicio.plusHours(1);
        when(f.agendas.existeConflitoHorario(inicio, fim, 1, null)).thenReturn(true);
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BusinessConflictException.class, () -> f.service.validarDisponibilidade(inicio, fim, 1, null));
    }

    @Test
    void registrarPagamentoDeveRejeitarAgendamentoNaoConcluido() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(1)).thenReturn(Optional.of(f.agendamento));
        var request = new school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest(1, 2);
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BusinessConflictException.class, () -> f.service.registrarPagamento(request));
    }

    @Test
    void existeAgendamentoDevePropagarErroDoRepositorio() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.existsById(1)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> f.service.existeAgendamento(1));
    }

    @Test
    void agendamentoEstaConcluidoDeveFalharQuandoNaoEncontrado() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> f.service.agendamentoEstaConcluido(9));
    }

    @Test
    void buscarPorIdDevePropagarErroDoRepositorio() {
        // Arrange
        var f = new Fixture();
        when(f.agendas.findById(1)).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> f.service.buscarPorId(1));
    }

    private static class Fixture {
        final AgendamentoRepository agendas = mock(AgendamentoRepository.class);
        final ServicoRepository servicos = mock(ServicoRepository.class);
        final StatusAgendamentoService status = mock(StatusAgendamentoService.class);
        final ClienteRepository clientes = mock(ClienteRepository.class);
        final PagamentoService pagamentos = mock(PagamentoService.class);
        final PagamentoRepository pagamentoRepository = mock(PagamentoRepository.class);
        final UsuarioRepository usuarios = mock(UsuarioRepository.class);
        final BloqueioRepository bloqueios = mock(BloqueioRepository.class);
        final AgendamentoServicoService vinculos = mock(AgendamentoServicoService.class);
        final AgendamentoMapper mapper = mock(AgendamentoMapper.class);
        final AgendamentoService service = new AgendamentoService(agendas, servicos, status, clientes, pagamentos, pagamentoRepository, usuarios, bloqueios, vinculos, mapper);
        final Cliente cliente = new Cliente();
        final Usuario barbeiro = new Usuario();
        final Servico servico = new Servico();
        final Pagamento pagamento = new Pagamento();
        final Agendamento agendamento = new Agendamento();
        final AgendamentoResponse response = mock(AgendamentoResponse.class);

        Fixture() {
            cliente.setId(1);
            cliente.setNome("Ana");
            barbeiro.setId(1);
            servico.setId(1);
            servico.setAtivo(true);
            servico.setValorAtual(BigDecimal.TEN);
            servico.setDuracaoAtual(30);
            pagamento.setId(1);
            pagamento.setValor(BigDecimal.TEN);
            pagamento.setPago(false);
            agendamento.setIdAgendamento(1);
            agendamento.setCliente(cliente);
            agendamento.setBarbeiro(barbeiro);
            agendamento.setPagamento(pagamento);
            agendamento.setHorarioInicio(LocalDateTime.of(2026, 1, 1, 10, 0));
            agendamento.setHorarioFim(LocalDateTime.of(2026, 1, 1, 10, 30));
            agendamento.setStatusAgendamento(new StatusAgendamento(1, "AGENDADO"));
            when(mapper.toResponse(any(), anyList())).thenReturn(response);
            when(vinculos.buscarPorAgendamentos(anyList())).thenReturn(List.of());
        }
    }
}
