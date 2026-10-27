
package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.dto.servico.ServicoRequest;
import school.sptech.apl_api_barbershop.dto.servico.ServicoResponse;
import school.sptech.apl_api_barbershop.entity.Servico;
import school.sptech.apl_api_barbershop.exception.BusinessConflictException;
import school.sptech.apl_api_barbershop.exception.NotFoundException;
import school.sptech.apl_api_barbershop.repository.AgendamentoServicoRepository;
import school.sptech.apl_api_barbershop.repository.ServicoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final AgendamentoServicoRepository agendamentoServicoRepository;

    public ServicoService(ServicoRepository repository, AgendamentoServicoRepository agendamentoServicoRepository) {
        this.repository = repository;
        this.agendamentoServicoRepository = agendamentoServicoRepository;
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

    @Transactional
    public void excluir(Integer id) {
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));

        if (agendamentoServicoRepository.existsByServicoId(id)) {
            throw new BusinessConflictException("Não é possível excluir um serviço que possui agendamentos");
        }

        repository.delete(servico);
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

