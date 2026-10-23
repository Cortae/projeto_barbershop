package school.sptech.apl_api_barbershop.mapper.usuario;

import school.sptech.apl_api_barbershop.dto.UsuarioResponse;
import school.sptech.apl_api_barbershop.entity.Usuario;

public class UsuarioMapper {
    public static UsuarioResponse toResponse(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCelular()
        );
    }
}
