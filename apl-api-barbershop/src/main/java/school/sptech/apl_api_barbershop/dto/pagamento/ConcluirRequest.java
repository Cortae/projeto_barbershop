package school.sptech.apl_api_barbershop.dto.pagamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ConcluirRequest {
    @NotNull
    @Positive
    private Integer formaPagamentoId;

    public ConcluirRequest() {
    }

    public Integer getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Integer formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }
}
