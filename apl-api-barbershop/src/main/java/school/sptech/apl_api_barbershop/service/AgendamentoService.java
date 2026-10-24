package school.sptech.apl_api_barbershop.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.sptech.apl_api_barbershop.dto.AgendamentoCadastroRequestDto;
import school.sptech.apl_api_barbershop.dto.servico.AtualizarServicosAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.RemarcarAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoResponse;
import school.sptech.apl_api_barbershop.dto.agendamento.EditarAgendamentoRequest;
import school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.exception.BadRequestException;
import school.sptech.apl_api_barbershop.exception.BusinessConflictException;
import school.sptech.apl_api_barbershop.exception.NotFoundException;
import school.sptech.apl_api_barbershop.mapper.agendamento.AgendamentoMapper;
import school.sptech.apl_api_barbershop.repository.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final StatusAgendamentoService statusAgendamentoService;
    private final ClienteRepository clienteRepository;
    private final PagamentoService pagamentoService;
    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BloqueioRepository bloqueioRepository;
    private final AgendamentoServicoService agendamentoServicoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ServicoRepository servicoRepository,
            StatusAgendamentoService statusAgendamentoService,
            ClienteRepository clienteRepository,
            PagamentoService pagamentoService,
            PagamentoRepository pagamentoRepository,
            UsuarioRepository usuarioRepository,
            BloqueioRepository bloqueioRepository,
            AgendamentoServicoService agendamentoServicoService,
            AgendamentoMapper agendamentoMapper
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.statusAgendamentoService = statusAgendamentoService;
        this.clienteRepository = clienteRepository;
        this.pagamentoService = pagamentoService;
        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.agendamentoServicoService = agendamentoServicoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarPorBarbeiro(Integer id) {
        if (!usuarioRepository.existsById(id)) throw new NotFoundException("Barbeiro não encontrado");
        return toResponse(agendamentoRepository.findAllByBarbeiro_IdOrderByHorarioInicioAsc(id));
    }

    @Transactional
    public AgendamentoResponse atualizarStatus(Integer id, String acao) {
        return toResponse(atualizarStatusEntidade(id, acao));
    }

    private Agendamento atualizarStatusEntidade(Integer id, String acao) {
        Agendamento agendamento = buscarEntidade(id);
        if (!statusAgendamentoService.existeStatusContaining(acao)) {
            throw new BadRequestException("Status não permitido");
        }
        agendamento.setStatusAgendamento(statusAgendamentoService.buscaStatus(acao));
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public AgendamentoResponse concluir(Integer id, Integer formaPagamentoId) {
        Agendamento agendamento = buscarEntidade(id);
        if (status(agendamento).equals("CANCELADO")) {
            throw new BusinessConflictException("Agendamento cancelado não pode ser concluído");
        }
        if (status(agendamento).equals("CONCLUIDO")) {
            throw new BusinessConflictException("Agendamento já foi concluído");
        }
        agendamento.setStatusAgendamento(statusAgendamentoService.buscaStatus("CONCLUIDO"));
        agendamentoRepository.save(agendamento);
        if (formaPagamentoId != null) {
            pagamentoService.marcarComoPago(agendamento.getPagamento().getId(), formaPagamentoId);
        }
        return toResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPeriodo(LocalDate de, LocalDate ate) {
        return toResponse(agendamentoRepository.findByHorarioInicioBetweenOrderByHorarioInicioAsc(
                de.atStartOfDay(), ate.atTime(LocalTime.MAX)));
    }

    public boolean existeCliente(Integer id) {
        return clienteRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarAgendamentosPorCliente(Integer id) {
        return toResponse(agendamentoRepository
                .findByCliente_IdAndStatusAgendamento_TituloIgnoreCaseOrderByHorarioInicioDesc(id, "CONCLUIDO"));
    }

    @Transactional
    public AgendamentoResponse cadastrar(@Valid AgendamentoCadastroRequestDto request) {
        Cliente cliente = clienteRepository.findById(request.getCliente())
                .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));
        Usuario barbeiro = usuarioRepository.findById(request.getBarbeiro())
                .orElseThrow(() -> new BadRequestException("Barbeiro não encontrado"));
        List<Servico> servicos = buscarServicosValidos(request.getServicos());
        Totais totais = calcularTotais(servicos);
        LocalDateTime fim = request.getInicio().plusMinutes(totais.getDuracao());
        validarDisponibilidade(request.getInicio(), fim, barbeiro.getId(), null);

        Pagamento pagamento = pagamentoService.registrar(totais.getValor());
        Agendamento agendamento = new Agendamento(
                null, request.getInicio(), fim, titulo(cliente, totais.getDuracao()),
                statusAgendamentoService.buscaStatus("AGENDADO"), barbeiro, cliente, pagamento
        );
        agendamento = agendamentoRepository.save(agendamento);
        agendamentoServicoService.vincularAgendamentoServico(agendamento, servicos);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse editar(Integer id, EditarAgendamentoRequest request) {
        Agendamento agendamento = buscarEntidade(id);
        validarEditavel(agendamento);
        Cliente cliente = clienteRepository.findById(request.getCliente())
                .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));
        List<Servico> servicos = buscarServicosValidos(request.getServicos());
        Totais totais = calcularTotais(servicos);
        LocalDateTime fim = request.getInicio().plusMinutes(totais.getDuracao());
        validarDisponibilidade(request.getInicio(), fim, agendamento.getBarbeiro().getId(), id);

        agendamento.setCliente(cliente);
        agendamento.setHorarioInicio(request.getInicio());
        agendamento.setHorarioFim(fim);
        agendamento.setTitulo(titulo(cliente, totais.getDuracao()));
        atualizarPagamentoPendente(agendamento, totais.getValor());
        agendamentoRepository.save(agendamento);
        agendamentoServicoService.atualizarServicos(agendamento, servicos);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse atualizarServicosAgendamento(AtualizarServicosAgendamentoRequestDto request) {
        Agendamento agendamento = buscarEntidade(request.getIdAgendamento());
        validarEditavel(agendamento);
        List<Servico> servicos = buscarServicosValidos(request.getServicos());
        Totais totais = calcularTotais(servicos);
        LocalDateTime fim = agendamento.getHorarioInicio().plusMinutes(totais.getDuracao());
        validarDisponibilidade(agendamento.getHorarioInicio(), fim, agendamento.getBarbeiro().getId(), agendamento.getIdAgendamento());

        agendamento.setHorarioFim(fim);
        agendamento.setTitulo(titulo(agendamento.getCliente(), totais.getDuracao()));
        atualizarPagamentoPendente(agendamento, totais.getValor());
        agendamentoRepository.save(agendamento);
        agendamentoServicoService.atualizarServicos(agendamento, servicos);
        return toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse remarcarAgendamento(@Valid RemarcarAgendamentoRequestDto request) {
        Agendamento agendamento = buscarEntidade(request.getIdAgendamento());
        validarEditavel(agendamento);
        long duracao = Duration.between(agendamento.getHorarioInicio(), agendamento.getHorarioFim()).toMinutes();
        LocalDateTime fim = request.getInicio().plusMinutes(duracao);
        validarDisponibilidade(request.getInicio(), fim, agendamento.getBarbeiro().getId(), agendamento.getIdAgendamento());
        agendamento.setHorarioInicio(request.getInicio());
        agendamento.setHorarioFim(fim);
        return toResponse(agendamentoRepository.save(agendamento));
    }

    public void validarDisponibilidade(LocalDateTime inicio, LocalDateTime fim, Integer barbeiroId, Integer ignorarId) {
        if (agendamentoRepository.existeConflitoHorario(inicio, fim, barbeiroId, ignorarId)) {
            throw new BusinessConflictException("Horário já ocupado para esse barbeiro");
        }
        if (bloqueioRepository.existsByUsuarioIdAndInicioLessThanAndFimGreaterThan(barbeiroId, fim, inicio)) {
            throw new BusinessConflictException("O horário coincide com um bloqueio da agenda");
        }
    }

    @Transactional
    public AgendamentoResponse registrarPagamento(PagarRequest request) {
        Agendamento agendamento = buscarEntidade(request.getIdAgendamento());
        if (!status(agendamento).equals("CONCLUIDO")) {
            throw new BusinessConflictException("Agendamento precisa estar concluído para pagar");
        }
        pagamentoService.marcarComoPago(agendamento.getPagamento().getId(), request.getFormaPagamentoId());
        return toResponse(agendamento);
    }

    public boolean existeAgendamento(Integer id) {
        return agendamentoRepository.existsById(id);
    }

    public boolean agendamentoEstaConcluido(Integer id) {
        return status(buscarEntidade(id)).equals("CONCLUIDO");
    }

    @Transactional(readOnly = true)
    public java.util.Optional<AgendamentoResponse> buscarPorId(Integer id) {
        return agendamentoRepository.findById(id).map(this::toResponse);
    }

    private Agendamento buscarEntidade(Integer id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));
    }

    private List<Servico> buscarServicosValidos(List<Integer> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(java.util.Objects::isNull)) {
            throw new BadRequestException("Informe ao menos um serviço");
        }
        if (ids.stream().distinct().count() != ids.size()) {
            throw new BadRequestException("A lista de serviços não pode conter itens repetidos");
        }
        Map<Integer, Servico> encontrados = servicoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Servico::getId, Function.identity()));
        if (encontrados.size() != ids.size()) {
            throw new BadRequestException("Um ou mais serviços não foram encontrados");
        }
        List<Servico> servicos = ids.stream().map(encontrados::get).toList();
        if (servicos.stream().anyMatch(servico -> !Boolean.TRUE.equals(servico.getAtivo()))) {
            throw new BadRequestException("Um ou mais serviços não estão disponíveis");
        }
        return servicos;
    }

    private Totais calcularTotais(List<Servico> servicos) {
        BigDecimal valor = servicos.stream().map(Servico::getValorAtual).reduce(BigDecimal.ZERO, BigDecimal::add);
        int duracao = servicos.stream().mapToInt(Servico::getDuracaoAtual).sum();
        return new Totais(valor, duracao);
    }

    private void atualizarPagamentoPendente(Agendamento agendamento, BigDecimal valor) {
        if (agendamento.getPagamento().isPago()) {
            throw new BusinessConflictException("Não é possível alterar serviços de um pagamento já realizado");
        }
        agendamento.getPagamento().setValor(valor);
        pagamentoRepository.save(agendamento.getPagamento());
    }

    private void validarEditavel(Agendamento agendamento) {
        if (List.of("CANCELADO", "CONCLUIDO", "NAO_COMPARECEU").contains(status(agendamento))) {
            throw new BusinessConflictException("Este agendamento não pode mais ser editado");
        }
    }

    private String status(Agendamento agendamento) {
        return agendamento.getStatusAgendamento().getTitulo().toUpperCase();
    }

    private String titulo(Cliente cliente, int duracao) {
        return cliente.getNome() + " - Duração: " + duracao + "min";
    }

    private AgendamentoResponse toResponse(Agendamento agendamento) {
        return toResponse(List.of(agendamento)).getFirst();
    }

    private List<AgendamentoResponse> toResponse(List<Agendamento> agendamentos) {
        List<Integer> ids = agendamentos.stream().map(Agendamento::getIdAgendamento).toList();
        Map<Integer, List<AgendamentoServico>> porAgendamento = agendamentoServicoService.buscarPorAgendamentos(ids)
                .stream().collect(Collectors.groupingBy(
                        item -> item.getAgendamento().getIdAgendamento(), LinkedHashMap::new, Collectors.toList()));
        return agendamentos.stream().map(agendamento -> agendamentoMapper.toResponse(
                agendamento, porAgendamento.getOrDefault(agendamento.getIdAgendamento(), List.of()))).toList();
    }

    private static class Totais {
        private final BigDecimal valor;
        private final int duracao;

        public Totais(BigDecimal valor, int duracao) {
            this.valor = valor;
            this.duracao = duracao;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public int getDuracao() {
            return duracao;
        }
    }
}
