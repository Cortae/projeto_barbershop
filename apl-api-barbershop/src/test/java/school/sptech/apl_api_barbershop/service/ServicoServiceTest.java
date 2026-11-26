package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.servico.ServicoRequest;
import school.sptech.apl_api_barbershop.entity.Servico;
import school.sptech.apl_api_barbershop.exception.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ServicoServiceTest {
    @Test
    void deveExecutarCrud() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var vinculos = mock(AgendamentoServicoRepository.class);
        var service = new ServicoService(repository, vinculos);
        var request = new ServicoRequest("Corte", BigDecimal.TEN, 30);
        var entidade = new Servico();
        entidade.setId(1);
        entidade.setTitulo("Corte");
        entidade.setValorAtual(BigDecimal.TEN);
        entidade.setDuracaoAtual(30);
        entidade.setAtivo(true);
        when(repository.save(any())).thenReturn(entidade);
        when(repository.findById(1)).thenReturn(Optional.of(entidade));
        when(repository.findAll()).thenReturn(List.of(entidade));
        // Act
        var criado = service.cadastrar(request);
        var editado = service.editar(1, request);
        var encontrado = service.buscar(1);
        var lista = service.listar(null);
        var alterado = service.alterarAtivo(1, false);
        service.excluir(1);
        // Assert
        assertEquals(1, criado.id());
        assertNotNull(editado);
        assertEquals(1, encontrado.id());
        assertEquals(1, lista.size());
        assertFalse(alterado.ativo());
        then(repository).should().delete(entidade);
    }

    @Test
    void deveRejeitarDuplicidade() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var service = new ServicoService(repository, mock(AgendamentoServicoRepository.class));
        var request = new ServicoRequest("Corte", BigDecimal.TEN, 30);
        when(repository.existsByTituloIgnoreCase("Corte")).thenReturn(true);
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(request));
        assertThrows(NotFoundException.class, () -> service.excluir(9));
    }

    @Test
    void editarDeveRetornarNullQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var service = new ServicoService(repository, mock(AgendamentoServicoRepository.class));
        when(repository.findById(9)).thenReturn(Optional.empty());
        // Act
        var resposta = service.editar(9, new ServicoRequest("Corte", BigDecimal.TEN, 30));
        // Assert
        assertNull(resposta);
        then(repository).should(never()).save(any());
    }

    @Test
    void buscarDeveRetornarNullQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var service = new ServicoService(repository, mock(AgendamentoServicoRepository.class));
        // Act / Assert
        assertNull(service.buscar(9));
        then(repository).should().findById(9);
    }

    @Test
    void listarDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var service = new ServicoService(repository, mock(AgendamentoServicoRepository.class));
        when(repository.findAll()).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.listar(null));
    }

    @Test
    void alterarAtivoDeveRetornarNullQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(ServicoRepository.class);
        var service = new ServicoService(repository, mock(AgendamentoServicoRepository.class));
        // Act / Assert
        assertNull(service.alterarAtivo(9, true));
        then(repository).should(never()).save(any());
    }
}
