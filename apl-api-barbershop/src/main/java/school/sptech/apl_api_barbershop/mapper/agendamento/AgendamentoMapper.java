package school.sptech.apl_api_barbershop.mapper.agendamento;

import org.springframework.stereotype.Component;
import school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoResponse;
import school.sptech.apl_api_barbershop.dto.pagamento.PagamentoResponse;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;
import school.sptech.apl_api_barbershop.entity.Cliente;

import java.util.List;

@Component
public class AgendamentoMapper {

    public AgendamentoResponse toResponse(
            Agendamento agendamento,
            List<AgendamentoServico> agendamentoServicos
    ) {
        return new AgendamentoResponse(
                agendamento.getIdAgendamento(),
                agendamento.getHorarioInicio(),
                agendamento.getHorarioFim(),
                agendamento.getTitulo(),
                agendamento.getStatusAgendamento().getTitulo(),
                agendamento.getBarbeiro().getId(),
                toClienteResponse(agendamento.getCliente()),
                agendamentoServicos.stream().map(this::toServicoResponse).toList(),
                PagamentoResponse.converter(agendamento.getPagamento())
        );
    }

    private AgendamentoResponse.ClienteResponse toClienteResponse(Cliente cliente) {
        return new AgendamentoResponse.ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getApelido(),
                cliente.getTelefone(),
                cliente.getNascimento()
        );
    }

    private AgendamentoResponse.ServicoResponse toServicoResponse(AgendamentoServico agendamentoServico) {
        return new AgendamentoResponse.ServicoResponse(
                agendamentoServico.getServico().getId(),
                agendamentoServico.getServico().getTitulo(),
                agendamentoServico.getValorUnitario(),
                agendamentoServico.getDuracao()
        );
    }
}
