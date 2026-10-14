package school.sptech.apl_api_barbershop.dto.pagamento;

import jakarta.validation.constraints.NotNull;

public class PagarRequest {
    @NotNull(message = "A forma de pagamento é obrigatória")
    private Integer formaPagamentoId;

    public PagarRequest() {
    }

    public Integer getFormaPagamentoId(){
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Integer formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }
}
