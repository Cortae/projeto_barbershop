package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioRequest;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioResponse;
import school.sptech.apl_api_barbershop.entity.Bloqueio;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.exception.BadRequestException;
import school.sptech.apl_api_barbershop.exception.BusinessConflictException;
import school.sptech.apl_api_barbershop.exception.NotFoundException;
import school.sptech.apl_api_barbershop.repository.BloqueioRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloqueioService {

    public static final String MENSAGEM_CONFLITO =
            "Já existe atendimento nesse horário. Remarque ou cancele antes de bloquear.";

    private final BloqueioRepository bloqueioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgendamentoConsulta agendamentoConsulta;

    public BloqueioService(
            BloqueioRepository bloqueioRepository,
            UsuarioRepository usuarioRepository,
            AgendamentoConsulta agendamentoConsulta
    ) {
        this.bloqueioRepository = bloqueioRepository;
        this.usuarioRepository = usuarioRepository;
        this.agendamentoConsulta = agendamentoConsulta;
    }

    @Transactional
    public BloqueioResponse criar(BloqueioRequest request) {
        validarPeriodo(request);
        Usuario usuario = buscarUsuario(request.usuarioId());
        if (agendamentoConsulta.existeAtendimentoNoPeriodo(request.usuarioId(), request.inicio(), request.fim())) {
            throw new BusinessConflictException(MENSAGEM_CONFLITO);
        }

        Bloqueio bloqueio = new Bloqueio();
        bloqueio.setUsuario(usuario);
        bloqueio.setInicio(request.inicio());
        bloqueio.setFim(request.fim());
        bloqueio.setMotivo(request.motivo());
        return resposta(bloqueioRepository.save(bloqueio));
    }

    @Transactional(readOnly = true)
    public List<BloqueioResponse> listarPorPeriodo(Long barbeiroId, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || barbeiroId == null) {
            throw new BadRequestException("barbeiroId, inicio e fim são obrigatórios");
        }
        if (fim.isBefore(inicio)) {
            throw new BadRequestException("O fim do período não pode ser anterior ao início");
        }
        buscarUsuario(barbeiroId);
        return bloqueioRepository.findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(
                        barbeiroId.intValue(), fim, inicio
                ).stream()
                .map(this::resposta)
                .toList();
    }

    @Transactional
    public void remover(Long id) {
        Bloqueio bloqueio = bloqueioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloqueio não encontrado"));
        bloqueioRepository.delete(bloqueio);
    }

    private void validarPeriodo(BloqueioRequest request) {
        if (request == null || request.usuarioId() == null || request.inicio() == null || request.fim() == null) {
            throw new BadRequestException("Usuário, início e fim são obrigatórios");
        }
        if (!request.fim().isAfter(request.inicio())) {
            throw new BadRequestException("O fim deve ser depois do início");
        }
        if (request.motivo() != null && request.motivo().length() > 100) {
            throw new BadRequestException("O motivo deve ter no máximo 100 caracteres");
        }
    }

    private Usuario buscarUsuario(Long usuarioId) {
        if (usuarioId > Integer.MAX_VALUE || usuarioId < Integer.MIN_VALUE) {
            throw new NotFoundException("Usuário não encontrado");
        }
        return usuarioRepository.findById(usuarioId.intValue())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private BloqueioResponse resposta(Bloqueio bloqueio) {
        return new BloqueioResponse(
                bloqueio.getIdBloqueio(),
                bloqueio.getUsuario().getId().longValue(),
                bloqueio.getInicio(),
                bloqueio.getFim(),
                bloqueio.getMotivo()
        );
    }
}
