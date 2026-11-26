package school.sptech.apl_api_barbershop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.apl_api_barbershop.dto.pagamento.PendenteResponse;
import school.sptech.apl_api_barbershop.service.PagamentoService;

import java.util.List;

@Tag(name = "Pagamentos", description = "Operações sobre pagamentos.")
@RestController
@RequestMapping("pagamentos/")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }


    @Operation(summary = "Listar pagamentos pendentes",
            description = "Devolve os atendimentos concluídos cujo pagamento ainda não foi feito.")
    @ApiResponse(responseCode = "200", description = "Lista de pendentes (pode ser vazia)",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PendenteResponse.class))))
    @GetMapping("/pendentes")
    public ResponseEntity<List<PendenteResponse>> listarPendentes(){
            return ResponseEntity.ok(pagamentoService.listarPendentes());
    }


}
