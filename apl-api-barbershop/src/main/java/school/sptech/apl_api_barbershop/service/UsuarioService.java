package school.sptech.apl_api_barbershop.service;

import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.apl_api_barbershop.dto.LoginRequest;
import school.sptech.apl_api_barbershop.dto.UsuarioRequest;
import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.entity.TipoUsuario;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.repository.TipoUsuarioRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.security.SecureRandom;

@Service
public class UsuarioService {

    private static final int TAMANHO_SENHA_TEMPORARIA = 12;
    private static final String CARACTERES_SENHA =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789@#$%";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final ExpedienteService expedienteService;
    private final JavaMailSender mailSender;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          ExpedienteService expedienteService,
                          JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.expedienteService = expedienteService;
        this.mailSender = mailSender;
    }

    public UsuarioResponse cadastrar(UsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "E-mail já cadastrado"
            );
        }

        if (request.getCelular() != null
                && !request.getCelular().isBlank()
                && usuarioRepository.existsByCelular(request.getCelular())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Celular já cadastrado"
            );
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository
                .findById(request.getTipoUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de usuário não encontrado"
                ));

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setCelular(request.getCelular());
        usuario.setSenha(request.getSenha());
        usuario.setTipoUsuario(tipoUsuario);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        if ("BARBEIRO".equalsIgnoreCase(
                usuarioSalvo.getTipoUsuario().getTitulo())) {

            expedienteService.inicializarPadrao(usuarioSalvo);
        }

        return new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getTipoUsuario().getTitulo()
        );
    }

    public UsuarioResponse buscarPorId(Integer id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario().getTitulo()
        );
    }


    public UsuarioResponse autenticar(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(
                request.getEmail()
        );

        if (usuario == null || !usuario.getSenha().equals(request.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email ou senha inválidos"
            );
        }

        if (!"ADMINISTRADOR".equalsIgnoreCase(
                usuario.getTipoUsuario().getTitulo())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acesso permitido somente para administradores"
            );
        }

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario().getTitulo()
        );
    }

    @Transactional
    public void enviarNovaSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "E-mail não cadastrado"
            );
        }

        String novaSenha = gerarSenhaAleatoria();
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(usuario.getEmail());
        mensagem.setSubject("Recuperação de senha - JR Barbershop");
        mensagem.setText(
                "Olá, " + usuario.getNome() + "!\n\n" +
                "Sua nova senha é: " + novaSenha + "\n\n" +
                "Recomendamos que você altere essa senha após entrar no sistema."
        );

        mailSender.send(mensagem);
    }

    private String gerarSenhaAleatoria() {
        StringBuilder senha = new StringBuilder(TAMANHO_SENHA_TEMPORARIA);

        for (int i = 0; i < TAMANHO_SENHA_TEMPORARIA; i++) {
            int indice = RANDOM.nextInt(CARACTERES_SENHA.length());
            senha.append(CARACTERES_SENHA.charAt(indice));
        }

        return senha.toString();
    }
}



