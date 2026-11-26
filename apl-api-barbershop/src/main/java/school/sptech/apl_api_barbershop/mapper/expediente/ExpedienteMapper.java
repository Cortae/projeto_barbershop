package school.sptech.apl_api_barbershop.mapper.expediente;

import org.springframework.stereotype.Component;
import school.sptech.apl_api_barbershop.dto.expediente.DiaExpedienteRequest;
import school.sptech.apl_api_barbershop.dto.expediente.DiaExpedienteResponse;
import school.sptech.apl_api_barbershop.dto.expediente.ExpedienteResponse;
import school.sptech.apl_api_barbershop.dto.expediente.JanelaFuncionamento;
import school.sptech.apl_api_barbershop.entity.Expediente;
import school.sptech.apl_api_barbershop.entity.Usuario;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Component
public final class ExpedienteMapper {

    public static Expediente toEntity(Usuario usuario, Expediente existente, DiaExpedienteRequest request) {
        Expediente expediente = existente == null ? new Expediente() : existente;
        expediente.setUsuario(usuario);
        expediente.setDiaSemana(request.getDiaSemana());
        expediente.setAberto(request.getAberto());
        expediente.setHoraAbertura(request.getHoraAbertura());
        expediente.setHoraFechamento(request.getHoraFechamento());
        expediente.setIntervaloInicio(request.getIntervaloInicio());
        expediente.setIntervaloFim(request.getIntervaloFim());
        return expediente;
    }

    public static Expediente toDefaultEntity(Usuario usuario, int diaSemana) {
        Expediente expediente = new Expediente();
        expediente.setUsuario(usuario);
        expediente.setDiaSemana(diaSemana);
        boolean aberto = diaSemana < 7;
        expediente.setAberto(aberto);
        expediente.setHoraAbertura(aberto ? LocalTime.of(9, 0) : null);
        expediente.setHoraFechamento(aberto ? LocalTime.of(18, 0) : null);
        expediente.setIntervaloInicio(aberto ? LocalTime.NOON : null);
        expediente.setIntervaloFim(aberto ? LocalTime.of(13, 0) : null);
        return expediente;
    }

    public static JanelaFuncionamento toJanelaFuncionamento(Expediente expediente) {
        return new JanelaFuncionamento(
                expediente.isAberto(),
                expediente.getHoraAbertura(),
                expediente.getHoraFechamento(),
                expediente.getIntervaloInicio(),
                expediente.getIntervaloFim()
        );
    }

    public static ExpedienteResponse toResponse(Integer usuarioId, List<Expediente> expedientes) {
        List<DiaExpedienteResponse> dias = expedientes.stream()
                .map(ExpedienteMapper::toDiaResponse)
                .sorted(Comparator.comparing(DiaExpedienteResponse::getDiaSemana))
                .toList();
        return new ExpedienteResponse(usuarioId, dias);
    }

    private static DiaExpedienteResponse toDiaResponse(Expediente expediente) {
        return new DiaExpedienteResponse(
                expediente.getDiaSemana(),
                expediente.isAberto(),
                expediente.getHoraAbertura(),
                expediente.getHoraFechamento(),
                expediente.getIntervaloInicio(),
                expediente.getIntervaloFim()
        );
    }
}
