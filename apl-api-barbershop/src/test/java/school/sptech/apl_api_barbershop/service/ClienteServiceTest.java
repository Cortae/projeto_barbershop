package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteRequest;
import school.sptech.apl_api_barbershop.entity.Cliente;
import school.sptech.apl_api_barbershop.repository.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ClienteServiceTest {
    @Test
    void cadastrarDeveRetornarCliente() {
        // Arrange
        var clientes = mock(ClienteRepository.class);
        var agendas = mock(AgendamentoRepository.class);
        var vinculos = mock(AgendamentoServicoRepository.class);
        var service = new ClienteService(clientes, agendas, vinculos);
        var request = new ClienteRequest("Ana", "Ani", "11999999999", LocalDate.of(2000, 1, 1));
        var cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Ana");
        when(clientes.save(any())).thenReturn(cliente);
        when(clientes.findById(1)).thenReturn(Optional.of(cliente));
        when(clientes.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(List.of(cliente));
        when(clientes.existsById(1)).thenReturn(true);
        when(agendas.findByClienteId(1)).thenReturn(List.of());
        // Act
        var criado = service.cadastrar(request);
        // Assert
        assertNotNull(criado);
    }

    @Test void editarDeveRetornarClienteAtualizado(){
        // Arrange
        var repository=mock(ClienteRepository.class); var service=new ClienteService(repository,mock(AgendamentoRepository.class),mock(AgendamentoServicoRepository.class)); var request=new ClienteRequest("Ana",null,"11999999999",null); var cliente=new Cliente(); when(repository.findById(1)).thenReturn(Optional.of(cliente)); when(repository.save(cliente)).thenReturn(cliente);
        // Act
        var resposta=service.editar(1,request);
        // Assert
        assertNotNull(resposta); then(repository).should().save(cliente);
    }
    @Test void buscarDeveRetornarCliente(){
        // Arrange
        var repository=mock(ClienteRepository.class); var service=new ClienteService(repository,mock(AgendamentoRepository.class),mock(AgendamentoServicoRepository.class)); var cliente=new Cliente(); when(repository.findById(1)).thenReturn(Optional.of(cliente));
        // Act
        var resposta=service.buscar(1);
        // Assert
        assertNotNull(resposta); then(repository).should().findById(1);
    }
    @Test void listarDeveRetornarClientes(){
        // Arrange
        var repository=mock(ClienteRepository.class); var service=new ClienteService(repository,mock(AgendamentoRepository.class),mock(AgendamentoServicoRepository.class)); var cliente=new Cliente(); cliente.setId(1); when(repository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(List.of(cliente));
        // Act
        var lista=service.listar(null);
        // Assert
        assertEquals(1,lista.size());
    }
    @Test void historicoDeveRetornarHistorico(){
        // Arrange
        var repository=mock(ClienteRepository.class); var service=new ClienteService(repository,mock(AgendamentoRepository.class),mock(AgendamentoServicoRepository.class)); when(repository.existsById(1)).thenReturn(true);
        // Act
        var lista=service.historico(1);
        // Assert
        assertTrue(lista.isEmpty());
    }
    @Test void excluirDeveRemoverCliente(){
        // Arrange
        var repository=mock(ClienteRepository.class); var agendas=mock(AgendamentoRepository.class); var service=new ClienteService(repository,agendas,mock(AgendamentoServicoRepository.class)); var cliente=new Cliente(); when(repository.findById(1)).thenReturn(Optional.of(cliente)); when(agendas.existsByClienteId(1)).thenReturn(false);
        // Act
        service.excluir(1);
        // Assert
        then(repository).should().delete(cliente);
    }

    @Test
    void cadastrarDevePropagarErroAoSalvar() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        when(repository.save(any())).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.cadastrar(new ClienteRequest("Ana", null, "11999999999", null)));
    }

    @Test
    void editarDeveRetornarNullQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        // Act / Assert
        assertNull(service.editar(9, new ClienteRequest("Ana", null, "11999999999", null)));
        then(repository).should(never()).save(any());
    }

    @Test
    void buscarDeveRetornarNullQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        // Act / Assert
        assertNull(service.buscar(9));
        then(repository).should().findById(9);
    }

    @Test
    void listarDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        when(repository.findAll(any(org.springframework.data.domain.Sort.class))).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.listar(null));
    }

    @Test
    void historicoDeveRetornarNullQuandoClienteNaoExiste() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        when(repository.existsById(9)).thenReturn(false);
        // Act / Assert
        assertNull(service.historico(9));
    }

    @Test
    void excluirDeveFalharQuandoClienteNaoExiste() {
        // Arrange
        var repository = mock(ClienteRepository.class);
        var service = new ClienteService(repository, mock(AgendamentoRepository.class), mock(AgendamentoServicoRepository.class));
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> service.excluir(9));
        then(repository).should(never()).delete(any());
    }
}
