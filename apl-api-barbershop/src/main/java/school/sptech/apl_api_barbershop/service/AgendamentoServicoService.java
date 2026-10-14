package school.sptech.apl_api_barbershop.service;

import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;
import school.sptech.apl_api_barbershop.entity.Servico;
import school.sptech.apl_api_barbershop.repository.AgendamentoServicoRepository;
import school.sptech.apl_api_barbershop.repository.ServicoRepository;

import java.util.List;

@Service
public class AgendamentoServicoService {

    private AgendamentoServicoRepository agendamentoServicoRepository;

    private ServicoRepository servicoRepository;

    public AgendamentoServicoService(AgendamentoServicoRepository agendamentoServicoRepository, ServicoRepository servicoRepository) {
        this.agendamentoServicoRepository = agendamentoServicoRepository;
        this.servicoRepository = servicoRepository;
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
}
