package school.sptech.apl_api_barbershop.mapper.bloqueio;

import org.springframework.stereotype.Component;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioRequest;
import school.sptech.apl_api_barbershop.dto.bloqueio.BloqueioResponse;
import school.sptech.apl_api_barbershop.entity.Bloqueio;
import school.sptech.apl_api_barbershop.entity.Usuario;

@Component
public final class BloqueioMapper {

    public static Bloqueio toEntity(BloqueioRequest request, Usuario usuario) {
        Bloqueio bloqueio = new Bloqueio();
        bloqueio.setUsuario(usuario);
        bloqueio.setInicio(request.getInicio());
        bloqueio.setFim(request.getFim());
        bloqueio.setMotivo(request.getMotivo());
        return bloqueio;
    }

    public static BloqueioResponse toResponse(Bloqueio bloqueio) {
        return new BloqueioResponse(
                bloqueio.getIdBloqueio(),
                bloqueio.getUsuario().getId(),
                bloqueio.getInicio(),
                bloqueio.getFim(),
                bloqueio.getMotivo()
        );
    }
}
