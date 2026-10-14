package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.pagamento.PagamentoResponse;
import school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest;
import school.sptech.apl_api_barbershop.service.PagamentoService;


@RestController
@RequestMapping("pagamentos/")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PatchMapping("{id}/pagar")
    public ResponseEntity<PagamentoResponse> marcarComoPago(@PathVariable Integer id, @Valid @RequestBody PagarRequest pagarRequest){
        PagamentoResponse resp = pagamentoService.marcarComoPago(id, pagarRequest);

        return ResponseEntity.ok(resp);
    }

}
