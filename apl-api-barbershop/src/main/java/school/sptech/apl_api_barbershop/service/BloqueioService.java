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
import school.sptech.apl_api_barbershop.mapper.bloqueio.BloqueioMapper;
import school.sptech.apl_api_barbershop.repository.BloqueioRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BloqueioService {

    private final BloqueioRepository bloqueioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgendamentoService agendamentoService;

    public BloqueioService(
            BloqueioRepository bloqueioRepository,
            UsuarioRepository usuarioRepository,
            AgendamentoService agendamentoService

    ) {
        this.bloqueioRepository = bloqueioRepository;
        this.usuarioRepository = usuarioRepository;
        this.agendamentoService = agendamentoService;
    }

    // Métodos principais
    @Transactional
    public BloqueioResponse criar(BloqueioRequest request) {
        validarPeriodo(request);

        Usuario usuario = buscarUsuario(request.usuarioId());

        if (bloqueioRepository.existsByUsuarioIdAndInicioLessThanAndFimGreaterThan(
                request.usuarioId(), request.fim(), request.inicio()
        )) {
            throw new BusinessConflictException("Já existe um bloqueio sobreposto para esse barbeiro.");
        }

        if (agendamentoService.validarDisponibilidade(request.inicio(), request.usuarioId())) {
            throw new BusinessConflictException("Já existe atendimento nesse horário. Remarque ou cancele antes de bloquear.");
        }

        Bloqueio bloqueio = BloqueioMapper.toEntity(request, usuario);
        return BloqueioMapper.toResponse(bloqueioRepository.save(bloqueio));
    }


    @Transactional(readOnly = true)
    public List<BloqueioResponse> listarPorPeriodo(Integer barbeiroId, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || barbeiroId == null) {
            throw new BadRequestException("barbeiroId, inicio e fim são obrigatórios");
        }
        if (fim.isBefore(inicio)) {
            throw new BadRequestException("O fim do período não pode ser anterior ao início");
        }
        buscarUsuario(barbeiroId);
        return bloqueioRepository.findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(
                        barbeiroId, fim, inicio
                ).stream()
                        .map(BloqueioMapper::toResponse)
                .toList();
    }


    @Transactional
    public void remover(Integer id) {
        Bloqueio bloqueio = bloqueioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloqueio não encontrado"));
        bloqueioRepository.delete(bloqueio);
    }


    // Métodos auxiliares
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

    private Usuario buscarUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new NotFoundException("Usuário não encontrado");
        }
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

}
