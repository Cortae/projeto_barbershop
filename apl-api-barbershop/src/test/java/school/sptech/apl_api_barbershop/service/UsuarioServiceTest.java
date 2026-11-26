package school.sptech.apl_api_barbershop.service;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import school.sptech.apl_api_barbershop.dto.*;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Test
    void deveCadastrarBuscarAutenticarERecuperarSenha() {
        // Arrange
        var usuarios = mock(UsuarioRepository.class);
        var tipos = mock(TipoUsuarioRepository.class);
        var expedientes = mock(ExpedienteService.class);
        var mail = mock(JavaMailSender.class);
        var service = new UsuarioService(usuarios, tipos, expedientes, mail);
        var request = mock(UsuarioRequest.class);
        var login = mock(LoginRequest.class);
        var tipo = new TipoUsuario();
        tipo.setTitulo("ADMINISTRADOR");
        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Ana");
        usuario.setEmail("a@b.com");
        usuario.setSenha("123");
        usuario.setTipoUsuario(tipo);
        when(request.getTipoUsuarioId()).thenReturn(1);
        when(tipos.findById(1)).thenReturn(Optional.of(tipo));
        when(usuarios.save(any())).thenReturn(usuario);
        when(usuarios.findById(1)).thenReturn(Optional.of(usuario));
        when(login.getEmail()).thenReturn("a@b.com");
        when(login.getSenha()).thenReturn("123");
        when(usuarios.findByEmail("a@b.com")).thenReturn(usuario);
        // Act
        var criado = service.cadastrar(request);
        var encontrado = service.buscarPorId(1);
        var autenticado = service.autenticar(login);
        service.enviarNovaSenha("a@b.com");
        // Assert
        assertEquals(1, criado.getId());
        assertEquals(1, encontrado.getId());
        assertEquals(1, autenticado.getId());
        assertEquals(12, usuario.getSenha().length());
        then(mail).should().send(any(org.springframework.mail.SimpleMailMessage.class));
    }

    @Test
    void cadastrarDeveFalharParaEmailDuplicado() {
        // Arrange
        var repository = mock(UsuarioRepository.class);
        var service = new UsuarioService(repository, mock(TipoUsuarioRepository.class), mock(ExpedienteService.class), mock(JavaMailSender.class));
        var request = mock(UsuarioRequest.class);
        when(request.getEmail()).thenReturn("a@b.com");
        when(repository.existsByEmail("a@b.com")).thenReturn(true);
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.cadastrar(request));
    }

    @Test
    void buscarDeveFalharQuandoUsuarioNaoExiste() {
        // Arrange
        var repository = mock(UsuarioRepository.class);
        var service = new UsuarioService(repository, mock(TipoUsuarioRepository.class), mock(ExpedienteService.class), mock(JavaMailSender.class));
        when(repository.findById(9)).thenReturn(Optional.empty());
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.buscarPorId(9));
    }

    @Test
    void autenticarDeveFalharComCredenciaisInvalidas() {
        // Arrange
        var repository = mock(UsuarioRepository.class);
        var service = new UsuarioService(repository, mock(TipoUsuarioRepository.class), mock(ExpedienteService.class), mock(JavaMailSender.class));
        var login = mock(LoginRequest.class);
        when(login.getEmail()).thenReturn("x@y.com");
        when(repository.findByEmail("x@y.com")).thenReturn(null);
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.autenticar(login));
    }

    @Test
    void enviarNovaSenhaDeveFalharQuandoEmailNaoExiste() {
        // Arrange
        var repository = mock(UsuarioRepository.class);
        var service = new UsuarioService(repository, mock(TipoUsuarioRepository.class), mock(ExpedienteService.class), mock(JavaMailSender.class));
        when(repository.findByEmail("x@y.com")).thenReturn(null);
        // Act / Assert
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.enviarNovaSenha("x@y.com"));
    }
}
