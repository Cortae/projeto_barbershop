package school.sptech.apl_api_barbershop.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagarRequest {

    @Schema(description = "Id do agendamento que está sendo pago", example = "3")
    @NotNull @Positive
    private Integer idAgendamento;

    @Schema(description = "Id da forma de pagamento (1=PIX, 2=DINHEIRO, 3=CARTAO_CREDITO, 4=CARTAO_DEBITO)", example = "1")
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
