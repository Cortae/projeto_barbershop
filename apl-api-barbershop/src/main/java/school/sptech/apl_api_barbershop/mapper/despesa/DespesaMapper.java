package school.sptech.apl_api_barbershop.mapper.despesa;

import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaResponse;
import school.sptech.apl_api_barbershop.entity.Despesa;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.mapper.usuario.UsuarioMapper;

public class DespesaMapper {
    public static Despesa toEntity(DespesaRequest despesaRequest, Usuario usuario) {
        return new Despesa(
                null,
                usuario,
                despesaRequest.getDescricao(),
                despesaRequest.getValor(),
                despesaRequest.getDataDespesa()
        );
    }

    public static DespesaResponse toResponse(Despesa despesa){
        return new DespesaResponse(
                despesa.getIdDespesa(),
                UsuarioMapper.toResponse(despesa.getUsuario()),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getDataDespesa()
        );
    }
}
