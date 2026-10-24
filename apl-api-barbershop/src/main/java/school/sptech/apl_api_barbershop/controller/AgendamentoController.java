package school.sptech.apl_api_barbershop.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.AgendamentoCadastroRequestDto;
import school.sptech.apl_api_barbershop.dto.AtualizarServicosAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.RemarcarAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.repository.AgendamentoRepository;
import school.sptech.apl_api_barbershop.service.AgendamentoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController( AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/barbeiro/{id}")
    public ResponseEntity<List<Agendamento>> buscarPorBarbeiro(@PathVariable Integer id){

        List<Agendamento> agendamentos = agendamentoService.buscarPorBarbeiro(id);

        if(agendamentos.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamentos);

    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<Agendamento> cancelarAgendamento(@PathVariable Integer id){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, "CANCELADO"));
    }

    @PatchMapping("/atualizar-status/{id}")
    public ResponseEntity<Agendamento> confirmarAgendamento(
            @PathVariable Integer id,
            @RequestParam(required = true) @Valid @NotBlank String status){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, status));
    }

    @PatchMapping("/concluir/{id}")
    public ResponseEntity<Agendamento> concluirAgendamento(@RequestBody(required = false) @Valid PagarRequest request, @PathVariable Integer id){
        agendamentoService.concluir(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/servicos")
    public ResponseEntity<Agendamento> atualizarServicosAgendamento(@RequestBody @Valid AtualizarServicosAgendamentoRequestDto request){

        return ResponseEntity.ok().body(agendamentoService.atualizarServicosAgendamento(request));
    }

    @PatchMapping("/horario")
    public ResponseEntity<Agendamento> remarcarAgendamento(@RequestBody @Valid RemarcarAgendamentoRequestDto request){

       return ResponseEntity.ok().body(agendamentoService.remarcarAgendamento(request));
    }

    @GetMapping("/periodo/")
    public ResponseEntity<List<Agendamento>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate de,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ate){

        if(de == null || ate == null){
            return ResponseEntity.badRequest().build();
        }else if(de.isAfter(ate)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(agendamentoService.listarPorPeriodo(de, ate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Integer id){
        Optional<Agendamento> agendamento = agendamentoService.buscarPorId(id);

        if(agendamento.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamento.get());
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Agendamento>> buscarAgendamentosPorCliente(@PathVariable Integer id){

        if(agendamentoService.existeCliente(id) == false){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamentoService.buscarAgendamentosPorCliente(id));
    }

    @PostMapping
    public ResponseEntity<Agendamento> cadastrar(@RequestBody @Valid AgendamentoCadastroRequestDto agendamento){
        return ResponseEntity.ok().body(agendamentoService.cadastrar(agendamento));
    }

    @PatchMapping("/pagar")
    public ResponseEntity<Agendamento> marcarComoPago(@RequestBody @Valid PagarRequest request){
        return ResponseEntity.ok().body(agendamentoService.registrarPagamento(request));
    }
}
