package school.sptech.apl_api_barbershop.dto.pagamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagarRequest {

    @NotNull @Positive
    private Integer idAgendamento;
    @NotNull(message = "A forma de pagamento é obrigatória") @Positive
    private Integer formaPagamentoId;

    public PagarRequest() {
    }

    public PagarRequest(Integer idAgendamento, Integer formaPagamentoId) {
        this.idAgendamento = idAgendamento;
        this.formaPagamentoId = formaPagamentoId;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Integer getFormaPagamentoId(){
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Integer formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }
}
