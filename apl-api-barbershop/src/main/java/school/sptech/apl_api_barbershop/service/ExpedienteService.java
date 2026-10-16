package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.dto.expediente.DiaExpedienteRequest;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteRequest;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteResponse;
import school.sptech.apl_api_barbershop.dto.expediente.JanelaFuncionamento;
import school.sptech.apl_api_barbershop.entity.Expediente;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.exception.BadRequestException;
import school.sptech.apl_api_barbershop.exception.NotFoundException;
import school.sptech.apl_api_barbershop.mapper.expediente.ExpedienteMapper;
import school.sptech.apl_api_barbershop.repository.ExpedienteRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpedienteService {

    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ExpedienteService(
            ExpedienteRepository expedienteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
    }


    // Métodos Principais
    @Transactional
    public ExpedienteResponse configurar(Integer usuarioId, ExpedienteRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        validar(request);

        Expediente[] expedientesExistentes = new Expediente[8];

        List<Expediente> expedientesDoUsuario =
                expedienteRepository.findByUsuarioIdOrderByDiaSemana(usuario.getId());

        for (Expediente expediente : expedientesDoUsuario) {
            Integer diaSemana = expediente.getDiaSemana();
            if (diaSemana != null && diaSemana >= 1 && diaSemana <= 7
                    && expedientesExistentes[diaSemana] == null) {
                expedientesExistentes[diaSemana] = expediente;
            }
        }

        List<Expediente> expedientesConfigurados = new ArrayList<>();
        for (DiaExpedienteRequest dia : request.dias()) {
            Expediente existente = expedientesExistentes[dia.diaSemana()];
            Expediente expediente = ExpedienteMapper.toEntity(usuario, existente, dia);
            expedientesConfigurados.add(expediente);
        }

        expedienteRepository.saveAll(expedientesConfigurados);
        return ExpedienteMapper.toResponse(usuarioId, expedientesConfigurados);
    }


    @Transactional(readOnly = true)
    public ExpedienteResponse consultar(Integer usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        List<Expediente> dias = expedienteRepository.findByUsuarioIdOrderByDiaSemana(usuario.getId());
        return ExpedienteMapper.toResponse(usuarioId, dias);
    }


    @Transactional(readOnly = true)
    public JanelaFuncionamento obterDoDia(Integer barbeiroId, LocalDate data) {
        Usuario usuario = buscarUsuario(barbeiroId);
        int diaSemana = data.getDayOfWeek().getValue();
        Expediente expediente = expedienteRepository.findByUsuarioIdAndDiaSemana(usuario.getId(), diaSemana)
                .orElseThrow(() -> new NotFoundException("Expediente do dia não encontrado"));
        return ExpedienteMapper.toJanelaFuncionamento(expediente);
    }


    @Transactional
    public void inicializarPadrao(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("O usuário precisa estar persistido para inicializar o expediente");
        }

        boolean[] diasCadastrados = new boolean[8];
        for (Expediente expediente : expedienteRepository.findByUsuarioIdOrderByDiaSemana(usuario.getId())) {
            Integer diaSemana = expediente.getDiaSemana();
            if (diaSemana != null && diaSemana >= 1 && diaSemana <= 7) {
                diasCadastrados[diaSemana] = true;
            }
        }

        List<Expediente> faltantes = new ArrayList<>();
        for (int diaSemana = 1; diaSemana <= 7; diaSemana++) {
            if (!diasCadastrados[diaSemana]) {
                faltantes.add(ExpedienteMapper.toDefaultEntity(usuario, diaSemana));
            }
        }

        if (!faltantes.isEmpty()) {
            expedienteRepository.saveAll(faltantes);
        }
    }


    // Metodos Auxiliares
    private Usuario buscarUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            throw new NotFoundException("Usuário não encontrado");
        }
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private void validar(ExpedienteRequest request) {
        if (request == null || request.dias() == null || request.dias().size() != 7) {
            throw new BadRequestException("O expediente deve conter exatamente 7 dias");
        }

        boolean[] diasRecebidos = new boolean[8];
        for (DiaExpedienteRequest dia : request.dias()) {
            if (dia == null || !dia.configuracaoValida()) {
                throw new BadRequestException("Dia da semana ou horários do expediente inválidos");
            }

            int diaSemana = dia.diaSemana();
            if (diasRecebidos[diaSemana]) {
                throw new BadRequestException("O dia da semana " + diaSemana + " foi informado mais de uma vez");
            }
            diasRecebidos[diaSemana] = true;
        }

        for (int diaSemana = 1; diaSemana <= 7; diaSemana++) {
            if (!diasRecebidos[diaSemana]) {
                throw new BadRequestException("O dia da semana " + diaSemana + " não foi informado");
            }
        }
    }

}
