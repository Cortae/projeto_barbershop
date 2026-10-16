package school.sptech.apl_api_barbershop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.apl_api_barbershop.dto.pagamento.FormaPagamentoResponse;
import school.sptech.apl_api_barbershop.repository.FormaPagamentoRepository;

import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
    private final FormaPagamentoRepository formaPagamentoRepository;

    public FormaPagamentoController(FormaPagamentoRepository formaPagamentoRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    @GetMapping
    public List<FormaPagamentoResponse> listar() {
        return formaPagamentoRepository.findAllByOrderByTituloAsc().stream()
                .map(FormaPagamentoResponse::from)
                .toList();
    }
}
