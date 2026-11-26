package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.entity.StatusAgendamento;
import school.sptech.apl_api_barbershop.repository.StatusAgendamentoRepository;

@Service
public class StatusAgendamentoService {

    private final StatusAgendamentoRepository statusAgendamentoRepository;

    public StatusAgendamentoService(StatusAgendamentoRepository statusAgendamentoRepository) {
        this.statusAgendamentoRepository = statusAgendamentoRepository;
    }

    public boolean existeStatusContaining(String status){
        return statusAgendamentoRepository.existsByTituloContainingIgnoreCase(status);
    }

    public StatusAgendamento buscaStatus(String status){
        return statusAgendamentoRepository.findByTituloContainingIgnoreCase(status);
    }
}
