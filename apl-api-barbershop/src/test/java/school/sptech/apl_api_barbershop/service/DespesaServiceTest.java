package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class DespesaServiceTest {
    @Test
    void cadastrarDeveRetornarDespesa() {
        // Arrange
        var despesas = mock(DespesaRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var service = new DespesaService(despesas, usuarios);
        var request = mock(DespesaRequest.class);
        var usuario = new Usuario();
        usuario.setId(1);
        var despesa = new Despesa(1, usuario, "Luz", BigDecimal.TEN, LocalDate.of(2026, 1, 1));
        when(request.getUsuarioId()).thenReturn(1);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(despesas.save(any())).thenReturn(despesa);
        when(despesas.findById(1)).thenReturn(Optional.of(despesa));
        when(despesas.findByDataDespesaBetween(any(), any())).thenReturn(List.of(despesa));
        when(despesas.existsById(1)).thenReturn(true);
        // Act
        var criado = service.cadastrar(request);
        // Assert
        assertNotNull(criado);
    }

    @Test
    void listarPorMesDeveRetornarResumo() {
        // Arrange
        var repository=mock(DespesaRepository.class); var service=new DespesaService(repository,mock(UsuarioRepository.class)); var usuario=new Usuario(); var despesa=new Despesa(1,usuario,"Luz",BigDecimal.TEN,LocalDate.of(2026,1,1)); when(repository.findByDataDespesaBetween(any(),any())).thenReturn(List.of(despesa));
        // Act
        var resumo=service.listarPorMes(YearMonth.of(2026,1));
        // Assert
        assertEquals(BigDecimal.TEN,resumo.getTotalMes());
    }

    @Test
    void editarDeveRetornarDespesaAtualizada() {
        // Arrange
        var repository=mock(DespesaRepository.class); var usuarios=mock(UsuarioRepository.class); var service=new DespesaService(repository,usuarios); var request=mock(DespesaRequest.class); var usuario=new Usuario(); var despesa=new Despesa(1,usuario,"Luz",BigDecimal.TEN,LocalDate.now()); when(request.getUsuarioId()).thenReturn(1); when(repository.findById(1)).thenReturn(Optional.of(despesa)); when(usuarios.findById(1)).thenReturn(Optional.of(usuario)); when(repository.save(despesa)).thenReturn(despesa);
        // Act
        var resposta=service.editar(1,request);
        // Assert
        assertNotNull(resposta); then(repository).should().save(despesa);
    }

    @Test
    void excluirDeveRemoverDespesa() {
        // Arrange
        var repository=mock(DespesaRepository.class); var service=new DespesaService(repository,mock(UsuarioRepository.class)); when(repository.existsById(1)).thenReturn(true);
        // Act
        service.excluir(1);
        // Assert
        then(repository).should().deleteById(1);
    }

    @Test
    void cadastrarDeveFalharSemUsuario() {
        // Arrange
        var usuarios = mock(UsuarioRepository.class);
        var service = new DespesaService(mock(DespesaRepository.class), usuarios);
        var request = mock(DespesaRequest.class);
        when(request.getUsuarioId()).thenReturn(9);
        when(usuarios.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.cadastrar(request));
    }

    @Test
    void listarPorMesDevePropagarErroDoRepositorio() {
        // Arrange
        var repository = mock(DespesaRepository.class);
        var service = new DespesaService(repository, mock(UsuarioRepository.class));
        when(repository.findByDataDespesaBetween(any(), any())).thenThrow(new RuntimeException("falha"));
        // Act / Assert
        assertThrows(RuntimeException.class, () -> service.listarPorMes(YearMonth.of(2026, 1)));
    }

    @Test
    void editarDeveFalharQuandoDespesaNaoExiste() {
        // Arrange
        var repository = mock(DespesaRepository.class);
        var service = new DespesaService(repository, mock(UsuarioRepository.class));
        when(repository.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.editar(9, mock(DespesaRequest.class)));
    }

    @Test
    void excluirDeveFalharQuandoDespesaNaoExiste() {
        // Arrange
        var repository = mock(DespesaRepository.class);
        var service = new DespesaService(repository, mock(UsuarioRepository.class));
        when(repository.existsById(9)).thenReturn(false);
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.excluir(9));
        then(repository).should(never()).deleteById(9);
    }
}
