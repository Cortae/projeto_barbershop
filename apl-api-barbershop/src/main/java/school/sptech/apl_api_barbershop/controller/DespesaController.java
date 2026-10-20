package school.sptech.apl_api_barbershop.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaMesResponse;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaRequest;
import school.sptech.apl_api_barbershop.dto.despesa.DespesaResponse;
import school.sptech.apl_api_barbershop.service.DespesaService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<DespesaMesResponse> listarPorMes(@RequestParam YearMonth mes) {
        return ResponseEntity.ok(despesaService.listarPorMes(mes));
    }
}
