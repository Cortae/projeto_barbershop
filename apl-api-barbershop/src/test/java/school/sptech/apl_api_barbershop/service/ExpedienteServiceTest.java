package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.expediente.*;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ExpedienteServiceTest {
    @Test
    void deveConfigurarConsultarObterDiaEInicializar() {
        // Arrange
        var repository = mock(ExpedienteRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var service = new ExpedienteService(repository, usuarios);
        var usuario = new Usuario();
        usuario.setId(1);
        var request = new ExpedienteRequest();
        request.setDias(List.of(dia(1, true), dia(2, true), dia(3, true), dia(4, true), dia(5, true), dia(6, true), dia(7, false)));
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(repository.findByUsuarioIdOrderByDiaSemana(1)).thenReturn(List.of());
        var expediente = new Expediente();
        expediente.setUsuario(usuario);
        expediente.setDiaSemana(1);
        expediente.setAberto(true);
        when(repository.findByUsuarioIdAndDiaSemana(1, 1)).thenReturn(Optional.of(expediente));
        // Act
        var configurado = service.configurar(1, request);
        var consultado = service.consultar(1);
        var janela = service.obterDoDia(1, LocalDate.of(2026, 1, 5));
        service.inicializarPadrao(usuario);
        // Assert
        assertEquals(7, configurado.getDias().size());
        assertTrue(consultado.getDias().isEmpty());
        assertTrue(janela.isAberto());
        then(repository).should(atLeastOnce()).saveAll(anyList());
    }

    private static DiaExpedienteRequest dia(int n, boolean aberto) {
        var d = new DiaExpedienteRequest();
        d.setDiaSemana(n);
        d.setAberto(aberto);
        if (aberto) {
            d.setHoraAbertura(LocalTime.of(9, 0));
            d.setHoraFechamento(LocalTime.of(18, 0));
        }
        return d;
    }

    @Test
    void configurarDeveRejeitarQuantidadeInvalidaDeDias() {
        // Arrange
        var usuarios = mock(UsuarioRepository.class);
        var service = new ExpedienteService(mock(ExpedienteRepository.class), usuarios);
        var usuario = new Usuario();
        usuario.setId(1);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        var request = new ExpedienteRequest();
        request.setDias(List.of());
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BadRequestException.class, () -> service.configurar(1, request));
    }

    @Test
    void consultarDeveFalharQuandoUsuarioNaoExiste() {
        // Arrange
        var service = new ExpedienteService(mock(ExpedienteRepository.class), mock(UsuarioRepository.class));
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> service.consultar(9));
    }

    @Test
    void obterDoDiaDeveFalharQuandoExpedienteNaoExiste() {
        // Arrange
        var usuarios = mock(UsuarioRepository.class);
        var repository = mock(ExpedienteRepository.class);
        var service = new ExpedienteService(repository, usuarios);
        var usuario = new Usuario();
        usuario.setId(1);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(repository.findByUsuarioIdAndDiaSemana(1, 1)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.NotFoundException.class, () -> service.obterDoDia(1, LocalDate.of(2026, 1, 5)));
    }

    @Test
    void inicializarPadraoDeveRejeitarUsuarioNaoPersistido() {
        // Arrange
        var service = new ExpedienteService(mock(ExpedienteRepository.class), mock(UsuarioRepository.class));
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> service.inicializarPadrao(new Usuario()));
    }
}
