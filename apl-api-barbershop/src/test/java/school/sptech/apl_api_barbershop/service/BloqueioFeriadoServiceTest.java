package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.client.BrasilApiClient;
import school.sptech.apl_api_barbershop.dto.feriado.FeriadoDto;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class BloqueioFeriadoServiceTest {
    @Test
    void deveImportarFeriadoParaBarbeiro() {
        // Arrange
        var client = mock(BrasilApiClient.class);
        var usuarios = mock(UsuarioRepository.class);
        var bloqueios = mock(BloqueioRepository.class);
        var agendamentos = mock(AgendamentoRepository.class);
        var service = new BloqueioFeriadoService(client, usuarios, bloqueios, agendamentos);
        var usuario = new Usuario();
        usuario.setId(1);
        when(client.listarFeriados(2026)).thenReturn(List.of(new FeriadoDto("2026-01-01", "Ano Novo", "national")));
        when(usuarios.findByTipoUsuario_TituloIgnoreCase("BARBEIRO")).thenReturn(List.of(usuario));
        // Act
        var resposta = service.importar(2026);
        // Assert
        assertEquals(1, resposta.getFeriadosEncontrados());
        then(bloqueios).should().save(any(Bloqueio.class));
    }

    @Test
    void deveRejeitarAnoInvalido() {
        // Arrange
        var service = new BloqueioFeriadoService(mock(BrasilApiClient.class), mock(UsuarioRepository.class), mock(BloqueioRepository.class), mock(AgendamentoRepository.class));
        // Act / Assert
        assertThrows(school.sptech.apl_api_barbershop.exception.BadRequestException.class, () -> service.importar(1899));
    }
}
