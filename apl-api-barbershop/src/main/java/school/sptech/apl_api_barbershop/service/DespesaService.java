package school.sptech.apl_api_barbershop.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaMesResponse;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaResponse;
import school.sptech.apl_api_barbershop.entity.Despesa;
import school.sptech.apl_api_barbershop.entity.Usuario;
import school.sptech.apl_api_barbershop.mapper.despesa.DespesaMapper;
import school.sptech.apl_api_barbershop.repository.DespesaRepository;
import school.sptech.apl_api_barbershop.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(DespesaRepository despesaRepository, UsuarioRepository usuarioRepository) {
        this.despesaRepository = despesaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DespesaResponse cadastrar(DespesaRequest request){
        Optional<Usuario> usuario = usuarioRepository.findById(request.getUsuarioId());
        if(usuario.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        Despesa despesaCadastrada = despesaRepository.save(DespesaMapper.toEntity(request, usuario.get()));

        return DespesaMapper.toResponse(despesaCadastrada);
    }

    public DespesaMesResponse listarPorMes(YearMonth mes) {
        LocalDate inicio = mes.atDay(1);
        LocalDate fim = mes.atEndOfMonth();

        List<Despesa> despesas = despesaRepository.findByDataDespesaBetween(inicio, fim);
        List<DespesaResponse> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Despesa despesa : despesas) {
            itens.add(DespesaMapper.toResponse(despesa));
            total = total.add(despesa.getValor());
        }

        return new DespesaMesResponse(itens, total);
    }
}
