package school.sptech.apl_api_barbershop.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.apl_api_barbershop.dto.pagamento.PagamentoResponse;
import school.sptech.apl_api_barbershop.dto.pagamento.PagarRequest;
import school.sptech.apl_api_barbershop.entity.Agendamento;
import school.sptech.apl_api_barbershop.entity.FormaPagamento;
import school.sptech.apl_api_barbershop.entity.Pagamento;
import school.sptech.apl_api_barbershop.repository.AgendamentoRepository;
import school.sptech.apl_api_barbershop.repository.FormaPagamentoRepository;
import school.sptech.apl_api_barbershop.repository.PagamentoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, FormaPagamentoRepository formaPagamentoRepository, AgendamentoRepository agendamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    public Pagamento registrar(BigDecimal valor){
        Pagamento pagamento = new Pagamento();

        pagamento.setValor(valor);
        pagamento.setPago(false);
        pagamento.setFormaPagamento(null);
        pagamento.setDataPagamento(null);

        return pagamentoRepository.save(pagamento);
    }

    public PagamentoResponse marcarComoPago(Integer pagamentoId, Integer idFormaPagamento){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(pagamentoId);
        Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(idFormaPagamento);

        if(pagamento.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Pagamento não encontrado"
            );
        }

        if(formaPagamento.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Forma de pagamento não encontrada"
            );
        }

        if(pagamento.get().isPago()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Este pagamento já foi registrado como pago"
            );
        }

        pagamento.get().setPago(true);
        pagamento.get().setFormaPagamento(formaPagamento.get());
        pagamento.get().setDataPagamento(LocalDateTime.now());

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento.get());
        return PagamentoResponse.converter(pagamentoSalvo);
    }


}
