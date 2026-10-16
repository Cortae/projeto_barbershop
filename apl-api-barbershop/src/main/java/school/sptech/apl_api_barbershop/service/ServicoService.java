
package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.dto.ServicoRequest;
import school.sptech.apl_api_barbershop.dto.ServicoResponse;
import school.sptech.apl_api_barbershop.entity.Servico;
import school.sptech.apl_api_barbershop.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoResponse cadastrar(ServicoRequest request) {

        if (repository.existsByTituloIgnoreCase(request.titulo())) {
            throw new IllegalArgumentException("Já existe um serviço com esse título.");
        }

        Servico servico = new Servico();

        servico.setTitulo(request.titulo());
        servico.setValorAtual(request.valorAtual());
        servico.setDuracaoAtual(request.duracaoAtual());
        servico.setAtivo(true);

        Servico servicoSalvo = repository.save(servico);

        return converterParaResponse(servicoSalvo);
    }

    public ServicoResponse editar(Integer id, ServicoRequest request) {

        Optional<Servico> servicoOptional = repository.findById(id);

        if (servicoOptional.isEmpty()) {
            return null;
        }

        if (repository.existsByTituloIgnoreCaseAndIdNot(request.titulo(), id)) {
            throw new IllegalArgumentException("Já existe um serviço com esse título.");
        }

        Servico servico = servicoOptional.get();

        servico.setTitulo(request.titulo());
        servico.setValorAtual(request.valorAtual());
        servico.setDuracaoAtual(request.duracaoAtual());

        Servico servicoAtualizado = repository.save(servico);

        return converterParaResponse(servicoAtualizado);
    }

    public ServicoResponse buscar(Integer id) {

        Optional<Servico> servicoOptional = repository.findById(id);

        if (servicoOptional.isEmpty()) {
            return null;
        }

        return converterParaResponse(servicoOptional.get());
    }

    public List<ServicoResponse> listar(Boolean ativo) {

        List<Servico> servicos;

        if (ativo == null) {
            servicos = repository.findAll();
        } else if (ativo) {
            servicos = repository.findByAtivoTrue();
        } else {
            servicos = repository.findByAtivoFalse();
        }

        return servicos.stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public ServicoResponse alterarAtivo(Integer id, Boolean ativo) {

        Optional<Servico> servicoOptional = repository.findById(id);

        if (servicoOptional.isEmpty()) {
            return null;
        }

        Servico servico = servicoOptional.get();

        servico.setAtivo(ativo);

        Servico servicoAtualizado = repository.save(servico);

        return converterParaResponse(servicoAtualizado);
    }

    private ServicoResponse converterParaResponse(Servico servico) {

        return new ServicoResponse(
                servico.getId(),
                servico.getTitulo(),
                servico.getValorAtual(),
                servico.getDuracaoAtual(),
                servico.getAtivo()
        );
    }
}

