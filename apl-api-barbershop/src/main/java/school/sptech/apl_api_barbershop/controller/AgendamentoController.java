package school.sptech.apl_api_barbershop.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.AgendamentoCadastroRequestDto;
import school.sptech.apl_api_barbershop.dto.AtualizarServicosAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.RemarcarAgendamentoRequestDto;
import school.sptech.apl_api_barbershop.dto.agendamento.AgendamentoResponse;
import school.sptech.apl_api_barbershop.dto.agendamento.EditarAgendamentoRequest;
import school.sptech.apl_api_barbershop.dto.pagamento.ConcluirRequest;
import school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest;
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
    public ResponseEntity<List<AgendamentoResponse>> buscarPorBarbeiro(@PathVariable Integer id){

        List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorBarbeiro(id);

        return ResponseEntity.ok().body(agendamentos);

    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<AgendamentoResponse> cancelarAgendamento(@PathVariable Integer id){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, "CANCELADO"));
    }

    @PatchMapping("/atualizar-status/{id}")
    public ResponseEntity<AgendamentoResponse> confirmarAgendamento(
            @PathVariable Integer id,
            @RequestParam(required = true) @Valid @NotBlank String status){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, status));
    }

    @PatchMapping("/concluir/{id}")
    public ResponseEntity<AgendamentoResponse> concluirAgendamento(
            @RequestBody(required = false) @Valid ConcluirRequest request,
            @PathVariable Integer id){
        return ResponseEntity.ok(agendamentoService.concluir(
                id, request == null ? null : request.getFormaPagamentoId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> editar(
            @PathVariable Integer id,
            @RequestBody @Valid EditarAgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.editar(id, request));
    }

    @PatchMapping("/servicos")
    public ResponseEntity<AgendamentoResponse> atualizarServicosAgendamento(@RequestBody @Valid AtualizarServicosAgendamentoRequestDto request){

        return ResponseEntity.ok().body(agendamentoService.atualizarServicosAgendamento(request));
    }

    @PatchMapping("/horario")
    public ResponseEntity<AgendamentoResponse> remarcarAgendamento(@RequestBody @Valid RemarcarAgendamentoRequestDto request){

       return ResponseEntity.ok().body(agendamentoService.remarcarAgendamento(request));
    }

    @GetMapping({"/periodo", "/periodo/"})
    public ResponseEntity<List<AgendamentoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate de,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ate){

        if(de.isAfter(ate)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(agendamentoService.listarPorPeriodo(de, ate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Integer id){
        Optional<AgendamentoResponse> agendamento = agendamentoService.buscarPorId(id);

        if(agendamento.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamento.get());
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<AgendamentoResponse>> buscarAgendamentosPorCliente(@PathVariable Integer id){

        if(agendamentoService.existeCliente(id) == false){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamentoService.buscarAgendamentosPorCliente(id));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> cadastrar(@RequestBody @Valid AgendamentoCadastroRequestDto agendamento){
        return ResponseEntity.status(201).body(agendamentoService.cadastrar(agendamento));
    }

    @PatchMapping("/pagar")
    public ResponseEntity<AgendamentoResponse> marcarComoPago(@RequestBody @Valid PagarRequest request){
        return ResponseEntity.ok().body(agendamentoService.registrarPagamento(request));
    }
}
