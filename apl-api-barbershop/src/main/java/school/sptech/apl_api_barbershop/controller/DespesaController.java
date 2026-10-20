package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaResponse;
import school.sptech.apl_api_barbershop.service.DespesaService;

import java.util.Optional;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    public ResponseEntity<DespesaResponse> cadastrar(@RequestBody @Valid DespesaRequest request){
        return ResponseEntity.status(201).body(despesaService.cadastrar(request));
    }
}
