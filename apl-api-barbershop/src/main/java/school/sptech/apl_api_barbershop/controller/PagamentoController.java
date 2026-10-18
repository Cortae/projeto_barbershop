package school.sptech.apl_api_barbershop.controller;

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


    @GetMapping("/pendentes")
    public ResponseEntity<List<PendenteResponse>> listarPendentes(){
            return ResponseEntity.ok(pagamentoService.listarPendentes());
    }


}
