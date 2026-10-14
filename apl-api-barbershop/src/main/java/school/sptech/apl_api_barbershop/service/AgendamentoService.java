package school.sptech.apl_api_barbershop.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.apl_api_barbershop.dto.AgendamentoCadastroRequestDto;
import school.sptech.apl_api_barbershop.dto.AtualizarServicosAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.RemarcarAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.entity.*;
import school.sptech.apl_api_barbershop.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {


    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final PagamentoService pagamentoService;
    private final UsuarioRepository usuarioRepository;
    private final AgendamentoServicoService agendamentoServicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              ServicoRepository servicoRepository,
                              StatusAgendamentoRepository statusAgendamentoRepository,
                              ClienteRepository clienteRepository,
                              PagamentoService pagamentoService,
                              UsuarioRepository usuarioRepository,
                              AgendamentoServicoService agendamentoServicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.pagamentoService = pagamentoService;
        this.usuarioRepository = usuarioRepository;
        this.agendamentoServicoService = agendamentoServicoService;
    }

    public List<Agendamento> buscarPorBarbeiro(Integer id) {
        return agendamentoRepository.findAllByBarbeiro(id);
    }

    public void atualizarStatus(Integer id, String acao) {

        StatusAgendamento status = statusAgendamentoRepository.findByTituloContainingIgnoreCase(acao);
        Agendamento agendamento = agendamentoRepository.findById(id).get();

        agendamento.setStatusAgendamento(status);

        agendamentoRepository.save(agendamento);

    }

    public void concluir(Integer id) {

    }

    public List<Agendamento> listarPorPeriodo(LocalDate de, LocalDate ate) {

        return agendamentoRepository.findByHorarioInicioBetweenOrderByHorarioInicioAsc( de.atStartOfDay(), ate.atTime(LocalTime.MAX));
    }

    public Optional<Agendamento> buscarPorId(Integer id) {

        return agendamentoRepository.findById(id);
    }

    public boolean existeCliente(Integer id) {
        return clienteRepository.existsById(id);
    }

    public List<Agendamento> buscarAgendamentosPorCliente(Integer id) {

        return agendamentoRepository.findByClienteId(id);
    }

    public Agendamento cadastrar(@Valid AgendamentoCadastroRequestDto agendamentoDto) {

        if(agendamentoRepository.existeAgendamentoAtivo(agendamentoDto.getInicio(), agendamentoDto.getBarbeiro())){
            throw new ResponseStatusException( HttpStatus.CONFLICT, "Horário já ocupado");
        }

        Agendamento agendamento = agendamentoRepository.save(cadastroRequestDtoToAgendamento(agendamentoDto));

        agendamentoServicoService.vincularAgendamentoServico(agendamento, buscarServicosRelacionados(agendamentoDto.getServicos()));

        return agendamento;

    }

    private List<Servico> buscarServicosRelacionados (List<Integer> idServicos){

        List<Servico> servicos = new ArrayList<>();

        idServicos.stream().forEach(
                servico -> {
                    Optional<Servico> busca = servicoRepository.findById(servico);
                    if(!busca.isEmpty()){
                        servicos.add(busca.get());
                    }
                });

        return servicos;
    }

    private Agendamento cadastroRequestDtoToAgendamento(AgendamentoCadastroRequestDto agendamento){


        Optional<Cliente> cliente = clienteRepository.findById(agendamento.getCliente());
        Optional<Usuario> barbeiro = usuarioRepository.findById(agendamento.getBarbeiro());

        List<Servico> servicos = buscarServicosRelacionados(agendamento.getServicos());

        if( cliente.isEmpty() ){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Cliente não encontrado");
        }else if( barbeiro.isEmpty() ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barbeiro não encontrado");
        }else if( servicos.isEmpty() ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço não encontrado");
        }

        BigDecimal valorTotal = new BigDecimal(0);
        Integer duracaoTotal = 0;

        for(Servico servico : servicos){
            duracaoTotal += servico.getDuracaoAtual();
            valorTotal = valorTotal.add(servico.getValorAtual());
        }

        Pagamento pagamento = pagamentoService.registrar(valorTotal);

        LocalDateTime fimAtendimento = agendamento.getInicio().plusMinutes((long) duracaoTotal);
        StatusAgendamento status = statusAgendamentoRepository.findByTituloContainingIgnoreCase("agendado");


        return new Agendamento(
                null,
                agendamento.getInicio(),
                fimAtendimento,
                "Isso é um teste",
                status,
                barbeiro.get(),
                cliente.get(),
                pagamento
        );
    }

    public Agendamento atualizarServicosAgendamento(AtualizarServicosAgendamentoRequestDto request) {

        Optional<Agendamento> agendamento = agendamentoRepository.findById(request.getIdAgendamento());

        if(agendamento.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento não encontrado");
        }

        List<Servico> servicos = buscarServicosRelacionados(request.getServicos());

        if(servicos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum serviço válido");
        }

        agendamentoServicoService.atualizarServicos(agendamento.get(), servicos);

        return agendamentoRepository.findById(request.getIdAgendamento()).get();

    }

    public Agendamento remarcarAgendamento(@Valid RemarcarAgendamentoRequestDto request) {

        if(!usuarioRepository.existsById(request.getIdAgendamento())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento não encontrado");
        }

        if(agendamentoRepository.existeAgendamentoAtivo(request.getInicio(), request.getIdAgendamento())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já ocupado para esse barbeiro.");
        }

        Agendamento agendamento = agendamentoRepository.findById(request.getIdAgendamento()).get();

        Integer duracaoMinutos = (agendamento.getHorarioFim().getMinute() - agendamento.getHorarioInicio().getMinute());

        agendamento.setHorarioInicio(request.getInicio());
        agendamento.setHorarioFim(agendamento.getHorarioInicio().plusMinutes(duracaoMinutos));

        return agendamentoRepository.save(agendamento);
    }

    public boolean validarDisponibilidade(LocalDateTime horarioInicio, Integer idBarbeiro){
        return agendamentoRepository.existeAgendamentoAtivo(horarioInicio, idBarbeiro);
    }
}
