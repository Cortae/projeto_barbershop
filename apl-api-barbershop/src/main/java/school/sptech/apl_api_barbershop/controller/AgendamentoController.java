package school.sptech.apl_api_barbershop.controller;


import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.AgendamentoCadastroRequestDto;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.repository.AgendamentoRepository;
import school.sptech.apl_api_barbershop.service.AgendamentoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private AgendamentoService agendamentoService;

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
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Integer id){
        agendamentoService.atualizarStatus(id, "CANCELADO");
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/concluir/{id}")
    public ResponseEntity<Void> concluirAgendamento(@PathVariable Integer id){
        agendamentoService.concluir(id);
        return ResponseEntity.ok().build();
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
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(agendamentoService.buscarAgendamentosPorCliente(id));
    }

    @PostMapping
    public ResponseEntity<Agendamento> cadastrar(@RequestBody @Valid AgendamentoCadastroRequestDto agendamento){
        return ResponseEntity.ok().body(agendamentoService.cadastrar(agendamento));
    }
}
