package school.sptech.apl_api_barbershop.service;

import org.apache.el.lang.ELArithmetic;
import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.entity.Pagamento;
import school.sptech.apl_api_barbershop.repository.PagamentoRepository;

import java.math.BigDecimal;

@Service
public class PagamentoService {
    public final PagamentoRepository repository;

    public PagamentoService(PagamentoRepository repository) {
        this.repository = repository;
    }

    public Pagamento registrar(BigDecimal valor){
        Pagamento pagamento = new Pagamento();

        pagamento.setValor(valor);
        pagamento.setPago(false);
        pagamento.setFormaPagamento(null);
        pagamento.setDataPagamento(null);

        return repository.save(pagamento);
    }


}
