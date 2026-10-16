package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;
import school.sptech.apl_api_barbershop.entity.Servico;
import school.sptech.apl_api_barbershop.repository.AgendamentoServicoRepository;
import school.sptech.apl_api_barbershop.repository.ServicoRepository;

import java.util.List;

@Service
public class AgendamentoServicoService {

    private final AgendamentoServicoRepository agendamentoServicoRepository;
    public AgendamentoServicoService(AgendamentoServicoRepository agendamentoServicoRepository) {
        this.agendamentoServicoRepository = agendamentoServicoRepository;
    }

    public void vincularAgendamentoServico(Agendamento agendamento, List<Servico> servicos){

        for(Servico servico : servicos){

            agendamentoServicoRepository.save(new AgendamentoServico(
                    null,
                    agendamento,
                    servico,
                    servico.getValorAtual(),
                    servico.getDuracaoAtual()
            ));
        }

    }


    @Transactional
    public void atualizarServicos(Agendamento agendamento, List<Servico> servicos){

        if(agendamentoServicoRepository.existsByAgendamentoIdAgendamento(agendamento.getIdAgendamento())){
            agendamentoServicoRepository.deleteByAgendamentoIdAgendamento(agendamento.getIdAgendamento());
            agendamentoServicoRepository.flush();
        }

        vincularAgendamentoServico(agendamento, servicos);
    }

    public List<AgendamentoServico> buscarPorAgendamentos(List<Integer> idsAgendamentos) {
        if (idsAgendamentos.isEmpty()) {
            return List.of();
        }
        return agendamentoServicoRepository.findAllByAgendamentoIdAgendamentoIn(idsAgendamentos);
    }
}
