package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioRequest;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class BloqueioServiceTest {
    @Test
    void criarDeveRetornarBloqueio() {
        // Arrange
        var repository = mock(BloqueioRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var agenda = mock(AgendamentoService.class);
        var service = new BloqueioService(repository, usuarios, agenda);
        var request = mock(BloqueioRequest.class);
        var inicio = LocalDateTime.now();
        var fim = inicio.plusHours(1);
        var usuario = new Usuario();
        usuario.setId(1);
        when(request.getUsuarioId()).thenReturn(1);
        when(request.getInicio()).thenReturn(inicio);
        when(request.getFim()).thenReturn(fim);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(repository.save(any())).thenAnswer(i -> {
            Bloqueio b = i.getArgument(0);
            b.setIdBloqueio(1);
            return b;
        });
        var bloqueio = new Bloqueio();
        bloqueio.setIdBloqueio(1);
        bloqueio.setUsuario(usuario);
        bloqueio.setInicio(inicio);
        bloqueio.setFim(fim);
        when(repository.findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(1, fim, inicio)).thenReturn(List.of(bloqueio));
        when(repository.findById(1)).thenReturn(Optional.of(bloqueio));
        // Act
        var criado = service.criar(request);
        // Assert
        assertEquals(1, criado.getId());
        then(agenda).should().validarDisponibilidade(inicio, fim, 1, null);
    }

    @Test
    void listarDeveRetornarBloqueios() {
        // Arrange
        var repository = mock(BloqueioRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var service = new BloqueioService(repository, usuarios, mock(AgendamentoService.class));
        var inicio = LocalDateTime.now(); var fim = inicio.plusHours(1); var usuario = new Usuario(); usuario.setId(1);
        var bloqueio = new Bloqueio(); bloqueio.setUsuario(usuario); bloqueio.setInicio(inicio); bloqueio.setFim(fim);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(repository.findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(1, fim, inicio)).thenReturn(List.of(bloqueio));
        // Act
        var lista = service.listarPorPeriodo(1, inicio, fim);
        // Assert
        assertEquals(1, lista.size()); then(repository).should().findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(1, fim, inicio);
    }

    @Test
    void removerDeveExcluirBloqueio() {
        // Arrange
        var repository = mock(BloqueioRepository.class); var service = new BloqueioService(repository, mock(UsuarioRepository.class), mock(AgendamentoService.class)); var bloqueio = new Bloqueio(); when(repository.findById(1)).thenReturn(Optional.of(bloqueio));
        // Act
        service.remover(1);
        // Assert
        then(repository).should().delete(bloqueio);
    }

    @Test
    void criarDeveRejeitarRequestInvalido() {
        // Arrange
        var service = new BloqueioService(mock(BloqueioRepository.class), mock(UsuarioRepository.class), mock(AgendamentoService.class));
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BadRequestException.class, () -> service.criar(null));
    }

    @Test
    void listarDeveRejeitarPeriodoInvalido() {
        // Arrange
        var service = new BloqueioService(mock(BloqueioRepository.class), mock(UsuarioRepository.class), mock(AgendamentoService.class));
        var inicio = LocalDateTime.now();
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BadRequestException.class, () -> service.listarPorPeriodo(1, inicio, inicio.minusMinutes(1)));
    }

    @Test
    void removerDeveFalharQuandoNaoEncontrado() {
        // Arrange
        var repository = mock(BloqueioRepository.class);
        var service = new BloqueioService(repository, mock(UsuarioRepository.class), mock(AgendamentoService.class));
        when(repository.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> service.remover(9));
        then(repository).should().findById(9);
    }
}
