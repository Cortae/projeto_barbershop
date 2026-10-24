package school.sptech.apl_api_barbershop.service;

import school.sptech.apl_api_barbershop.dto.cliente.ClienteHistoricoResponse;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteItemResponse;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteRequest;
import school.sptech.apl_api_barbershop.dto.cliente.ClienteResponse;
import school.sptech.apl_api_barbershop.entity.Cliente;
import school.sptech.apl_api_barbershop.repository.ClienteRepository;

import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.repository.AgendamentoRepository;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import school.sptech.apl_api_barbershop.entity.Pagamento;

import java.util.List;
import java.util.Optional;

import school.sptech.apl_api_barbershop.dto.cliente.ClienteHistoricoServicoResponse;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;
import school.sptech.apl_api_barbershop.repository.AgendamentoServicoRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoServicoRepository agendamentoServicoRepository;

    public ClienteService(
            ClienteRepository repository,
            AgendamentoRepository agendamentoRepository,
            AgendamentoServicoRepository agendamentoServicoRepository
    ) {
        this.repository = repository;
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoServicoRepository = agendamentoServicoRepository;
    }

    public ClienteResponse cadastrar(ClienteRequest request) {

        boolean celularRepetido = repository.existsByTelefone(request.celular());

        Cliente cliente = new Cliente();

        cliente.setNome(request.nome());
        cliente.setApelido(request.apelido());
        cliente.setTelefone(request.celular());
        cliente.setNascimento(request.nascimento());

        Cliente clienteSalvo = repository.save(cliente);

        String aviso = null;

        if (celularRepetido) {
            aviso = "Este celular já está cadastrado em outro cliente.";
        }

        return converterParaResponse(clienteSalvo, aviso);
    }

    public ClienteResponse editar(Integer id, ClienteRequest request) {

        Optional<Cliente> clienteOptional = repository.findById(id);

        if (clienteOptional.isEmpty()) {
            return null;
        }

        Cliente cliente = clienteOptional.get();

        cliente.setNome(request.nome());
        cliente.setApelido(request.apelido());
        cliente.setTelefone(request.celular());
        cliente.setNascimento(request.nascimento());

        Cliente clienteAtualizado = repository.save(cliente);

        return converterParaResponse(clienteAtualizado, null);
    }

    public ClienteResponse buscar(Integer id) {

        Optional<Cliente> clienteOptional = repository.findById(id);

        if (clienteOptional.isEmpty()) {
            return null;
        }

        return converterParaResponse(clienteOptional.get(), null);
    }

    public List<ClienteItemResponse> listar(String busca) {

        Sort ordenacao = Sort.by("nome").ascending();

        List<Cliente> clientes;

        if (busca == null || busca.isBlank()) {
            clientes = repository.findAll(ordenacao);
        } else {
            clientes = repository
                    .findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCase(
                            busca,
                            busca,
                            ordenacao
                    );
        }

        return clientes.stream()
                .map(cliente -> new ClienteItemResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        buscarUltimoCorte(cliente.getId()),
                        buscarTotalPendente(cliente.getId())
                ))
                .toList();
    }

    private LocalDateTime buscarUltimoCorte(Integer clienteId) {

        List<Agendamento> agendamentos =
                agendamentoRepository.findByClienteId(clienteId);

        LocalDateTime ultimoCorte = null;

        for (Agendamento agendamento : agendamentos) {

            if (agendamento.getStatusAgendamento() != null
                    && "CONCLUIDO".equalsIgnoreCase(
                    agendamento.getStatusAgendamento().getTitulo()
            )) {

                LocalDateTime data = agendamento.getHorarioInicio();

                if (data != null &&
                        (ultimoCorte == null || data.isAfter(ultimoCorte))) {

                    ultimoCorte = data;
                }
            }
        }

        return ultimoCorte;
    }

    private BigDecimal buscarTotalPendente(Integer clienteId) {

        List<Agendamento> agendamentos =
                agendamentoRepository.findByClienteId(clienteId);

        BigDecimal totalPendente = BigDecimal.ZERO;

        for (Agendamento agendamento : agendamentos) {

            if (agendamento.getStatusAgendamento() == null ||
                    !"CONCLUIDO".equalsIgnoreCase(
                            agendamento.getStatusAgendamento().getTitulo()
                    )) {
                continue;
            }

            Pagamento pagamento = agendamento.getPagamento();

            if (pagamento != null
                    && !pagamento.isPago()
                    && pagamento.getValor() != null) {

                totalPendente = totalPendente.add(pagamento.getValor());
            }
        }

        return totalPendente;
    }

    private ClienteResponse converterParaResponse(Cliente cliente, String aviso) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getApelido(),
                cliente.getTelefone(),
                cliente.getNascimento(),
                aviso
        );
    }

    public List<ClienteHistoricoResponse> historico(Integer clienteId) {

        if (!repository.existsById(clienteId)) {
            return null;
        }

        List<Agendamento> agendamentos =
                agendamentoRepository.findByClienteId(clienteId);

        List<ClienteHistoricoResponse> historico = new ArrayList<>();

        for (Agendamento agendamento : agendamentos) {

            Pagamento pagamento = agendamento.getPagamento();

            String status = agendamento.getStatusAgendamento() != null
                    ? agendamento.getStatusAgendamento().getTitulo()
                    : null;

            BigDecimal valorPagamento = pagamento != null
                    ? pagamento.getValor()
                    : null;

            Boolean pago = pagamento != null
                    ? pagamento.isPago()
                    : null;

            List<AgendamentoServico> agendamentoServicos =
                    agendamentoServicoRepository.findByAgendamentoIdAgendamento(
                            agendamento.getIdAgendamento()
                    );

            List<ClienteHistoricoServicoResponse> servicos = new ArrayList<>();

            for (AgendamentoServico item : agendamentoServicos) {

                servicos.add(new ClienteHistoricoServicoResponse(
                        item.getServico().getId(),
                        item.getServico().getTitulo(),
                        item.getValorUnitario(),
                        item.getDuracao()
                ));
            }

            ClienteHistoricoResponse itemHistorico = new ClienteHistoricoResponse(
                    agendamento.getIdAgendamento(),
                    agendamento.getTitulo(),
                    agendamento.getHorarioInicio(),
                    agendamento.getHorarioFim(),
                    status,
                    valorPagamento,
                    pago,
                    servicos
            );

            historico.add(itemHistorico);
        }

        historico.sort((a, b) -> {
            if (a.horarioInicio() == null) return 1;
            if (b.horarioInicio() == null) return -1;
            return b.horarioInicio().compareTo(a.horarioInicio());
        });

        return historico;
    }
}