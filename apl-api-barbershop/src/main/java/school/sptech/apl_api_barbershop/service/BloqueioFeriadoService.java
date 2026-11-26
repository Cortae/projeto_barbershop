package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.client.BrasilApiClient;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioRequest;
import school.sptech.apl_api_barbershop.dto.bloqueio.ConflitoFeriadoResponse;
import school.sptech.apl_api_barbershop.dto.bloqueio.ImportacaoFeriadosResponse;
import school.sptech.apl_api_barbershop.dto.feriado.FeriadoDto;
import school.sptech.apl_api_barbershop.entity.Bloqueio;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.exception.BadRequestException;
import school.sptech.apl_api_barbershop.mapper.bloqueio.BloqueioMapper;
import school.sptech.apl_api_barbershop.repository.AgendamentoRepository;
import school.sptech.apl_api_barbershop.repository.BloqueioRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BloqueioFeriadoService {

    private static final int ANO_MINIMO = 1900;
    private static final int ANO_MAXIMO = 2199;
    private static final String TIPO_BARBEIRO = "BARBEIRO";
    private static final int TAMANHO_MAXIMO_MOTIVO = 100;

    private final BrasilApiClient brasilApiClient;
    private final UsuarioRepository usuarioRepository;
    private final BloqueioRepository bloqueioRepository;
    private final AgendamentoRepository agendamentoRepository;

    public BloqueioFeriadoService(
            BrasilApiClient brasilApiClient,
            UsuarioRepository usuarioRepository,
            BloqueioRepository bloqueioRepository,
            AgendamentoRepository agendamentoRepository
    ) {
        this.brasilApiClient = brasilApiClient;
        this.usuarioRepository = usuarioRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    @Transactional
    public ImportacaoFeriadosResponse importar(int ano) {
        validarAno(ano);
        List<FeriadoDto> feriados = brasilApiClient.listarFeriados(ano);
        List<Usuario> barbeiros = usuarioRepository.findByTipoUsuario_TituloIgnoreCase(TIPO_BARBEIRO);
        List<ConflitoFeriadoResponse> conflitos = new ArrayList<>();
        int bloqueiosCriados = 0;
        int bloqueiosJaExistentes = 0;

        for (FeriadoDto feriado : feriados) {
            LocalDate data = LocalDate.parse(feriado.getDate());
            LocalDateTime inicio = data.atStartOfDay();
            LocalDateTime fim = data.atTime(LocalTime.of(23, 59, 59));

            for (Usuario barbeiro : barbeiros) {
                if (bloqueioRepository.existsByUsuarioIdAndInicioLessThanAndFimGreaterThan(
                        barbeiro.getId(), fim, inicio
                )) {
                    bloqueiosJaExistentes++;
                    continue;
                }

                if (agendamentoRepository.existeConflitoHorario(inicio, fim, barbeiro.getId(), null)) {
                    conflitos.add(new ConflitoFeriadoResponse(barbeiro.getId(), data, feriado.getName()));
                    continue;
                }

                BloqueioRequest request = criarRequest(barbeiro.getId(), inicio, fim, feriado.getName());
                Bloqueio bloqueio = BloqueioMapper.toEntity(request, barbeiro);
                bloqueioRepository.save(bloqueio);
                bloqueiosCriados++;
            }
        }

        return new ImportacaoFeriadosResponse(
                ano,
                feriados.size(),
                barbeiros.size(),
                bloqueiosCriados,
                bloqueiosJaExistentes,
                conflitos
        );
    }

    private void validarAno(int ano) {
        if (ano < ANO_MINIMO || ano > ANO_MAXIMO) {
            throw new BadRequestException("O ano deve estar entre 1900 e 2199.");
        }
    }

    private BloqueioRequest criarRequest(
            Integer barbeiroId,
            LocalDateTime inicio,
            LocalDateTime fim,
            String nomeFeriado
    ) {
        String motivo = "Feriado: " + nomeFeriado;
        if (motivo.length() > TAMANHO_MAXIMO_MOTIVO) {
            motivo = motivo.substring(0, TAMANHO_MAXIMO_MOTIVO);
        }

        BloqueioRequest request = new BloqueioRequest();
        request.setUsuarioId(barbeiroId);
        request.setInicio(inicio);
        request.setFim(fim);
        request.setMotivo(motivo);
        return request;
    }
}
