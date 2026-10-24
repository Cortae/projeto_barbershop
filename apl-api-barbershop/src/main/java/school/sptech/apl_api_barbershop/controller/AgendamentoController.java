package school.sptech.apl_api_barbershop.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Agendamentos", description = "Operações sobre agendamentos.")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController( AgendamentoService agendamentoService){
        this.agendamentoService = agendamentoService;
    }

    @Operation(
            summary = "Buscar agendamentos por barbeiro",
            description = "Endpoint responsável por listar os agendamentos de um barbeiro com o id enviado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Barbeiro encontrado"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado", content = @Content)
    })
    @GetMapping("/barbeiro/{id}")
    public ResponseEntity<List<AgendamentoResponse>> buscarPorBarbeiro(
            @Parameter(description = "Identificador do barbeiro", example = "1")
            @PathVariable Integer id){

        List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorBarbeiro(id);

        return ResponseEntity.ok().body(agendamentos);

    }

    @Operation(
            summary = "Cancelar agendamento",
            description = "Endpoint resposável por cancelar um agendamento de acordo com o id enviado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancelamento efetuado"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content)
    })
    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<AgendamentoResponse> cancelarAgendamento(
            @Parameter(description = "Identificador do agendamento", example = "1")
            @PathVariable Integer id){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, "CANCELADO"));
    }

    @Operation(
            summary = "Atualizar status",
            description = "Endpoint responsável por atualizar o status de um agendamento de acordo com o id e status enviado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="400", description = "Status não permitido", content = @Content)
    })
    @PatchMapping("/atualizar-status/{id}")
    public ResponseEntity<AgendamentoResponse> confirmarAgendamento(
            @Parameter(description = "Identificador do agendamento", example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Status para atualizar", example = "NAO_COMPARECEU")
            @RequestParam(required = true) @Valid @NotBlank String status){
        return ResponseEntity.ok().body(agendamentoService.atualizarStatus(id, status));
    }

    @Operation(
            summary = "Concluir agendamento",
            description = "Endpoint responsável por concluir um agendamento de acordo com o id enviado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado para concluído"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="400", description = "Conflito no horário do agendamento", content = @Content)
    })
    @PatchMapping("/concluir/{id}")
    public ResponseEntity<AgendamentoResponse> concluirAgendamento(
            @RequestBody(required = false) @Valid ConcluirRequest request,
            @Parameter(description = "Identificador do agendamento", example = "1")
            @PathVariable Integer id){
        return ResponseEntity.ok(agendamentoService.concluir(
                id, request == null ? null : request.getFormaPagamentoId()));
    }

    @Operation(
            summary = "Atualizar agendamento",
            description = "Endpoint responsável por atualizar um agendamento de acordo com o id enviado no path e os dados do body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="400", description = "Formato inválido para agendamento", content = @Content),
            @ApiResponse(responseCode="409", description = "Conflito no horário de agendamento", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> editar(
            @Parameter(description = "Identificador do agendamento", example = "1")
            @PathVariable Integer id,
            @RequestBody @Valid EditarAgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.editar(id, request));
    }

    @Operation(
            summary = "Atualizar serviços",
            description = "Endpoint responsável por atualizar a lista de serviços relacionados a um agendamento de acordo com o body passado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serviços vinculados ao agendamento atualizados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="400", description = "Formado inválido para agendamento", content = @Content)
    })
    @PatchMapping("/servicos")
    public ResponseEntity<AgendamentoResponse> atualizarServicosAgendamento(@RequestBody @Valid AtualizarServicosAgendamentoRequestDto request){

        return ResponseEntity.ok().body(agendamentoService.atualizarServicosAgendamento(request));
    }

    @Operation(
            summary = "Remarcar agendamento",
            description = "Endpoint responsável por remarcar um agendamento de acordo com o body passado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento remarcado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="409", description = "Conflito no horário do agendamento", content = @Content)
    })
    @PatchMapping("/horario")
    public ResponseEntity<AgendamentoResponse> remarcarAgendamento(@RequestBody @Valid RemarcarAgendamentoRequestDto request){

       return ResponseEntity.ok().body(agendamentoService.remarcarAgendamento(request));
    }

    @Operation(
            summary = "Listar por período",
            description = "Endpoint responsável por listar agendamento de um determinado período selecionado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Periodo informado inválido", content = @Content)
    })
    @GetMapping({"/periodo", "/periodo/"})
    public ResponseEntity<List<AgendamentoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate de,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ate){

        if(de.isAfter(ate)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(agendamentoService.listarPorPeriodo(de, ate));
    }

    @Operation(
            summary = "Buscar agendamento por ID",
            description = "Endpoint responsável por buscar um agendamento especifíco por meio do id informado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento buscado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(
            @Parameter(description = "Identificador do agendamento", example = "1")
            @PathVariable Integer id){
        Optional<AgendamentoResponse> agendamento = agendamentoService.buscarPorId(id);

        if(agendamento.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamento.get());
    }

    @Operation(
            summary = "Buscar agendamentos por cliente",
            description = "Endpoint responsável por buscar os agendamentos concluídos de um cliente especifico via id passado no path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<AgendamentoResponse>> buscarAgendamentosPorCliente(
            @Parameter(description = "Identificador do Cliente", example = "1")
            @PathVariable Integer id){

        if(agendamentoService.existeCliente(id) == false){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(agendamentoService.buscarAgendamentosPorCliente(id));
    }

    @Operation(
            summary = "Cadastrar agendamento",
            description = "Endpoint responsável por cadastrar um agendamento por meio do body passado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Agendamento com formato inválido", content = @Content),
            @ApiResponse(responseCode="409", description = "Conflito no horário do agendamento", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AgendamentoResponse> cadastrar(@RequestBody @Valid AgendamentoCadastroRequestDto agendamento){
        return ResponseEntity.status(201).body(agendamentoService.cadastrar(agendamento));
    }

    @Operation(
            summary = "Marcar agendamento como pago",
            description = "Endpoint responsável por marcar um agendamento como pago."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento marcado como pago com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content),
            @ApiResponse(responseCode="400", description = "O agendamento precisa estar concluido para ser pago", content = @Content)
    })
    @PatchMapping("/pagar")
    public ResponseEntity<AgendamentoResponse> marcarComoPago(@RequestBody @Valid PagarRequest request){
        return ResponseEntity.ok().body(agendamentoService.registrarPagamento(request));
    }
}
