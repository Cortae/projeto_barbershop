package school.sptech.apl_api_barbershop.service;

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

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final ExpedienteService expedienteService;

    public UsuarioService(UsuarioRepository usuarioRepository, TipoUsuarioRepository tipoUsuarioRepository, ExpedienteService expedienteService) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.expedienteService = expedienteService;
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

}



