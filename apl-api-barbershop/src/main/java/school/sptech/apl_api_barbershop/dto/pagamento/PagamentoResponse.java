package school.sptech.apl_api_barbershop.dto.pagamento;

import school.sptech.apl_api_barbershop.entity.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponse {
    private Integer id;
    private BigDecimal valor;
    private boolean pago;
    private LocalDateTime dataPagamento;
    private Integer formaPagamentoId;
    private String formaPagamento;

    public PagamentoResponse(Integer id, BigDecimal valor, boolean pago, LocalDateTime dataPagamento, Integer formaPagamentoId, String formaPagamento) {
        this.id = id;
        this.valor = valor;
        this.pago = pago;
        this.dataPagamento = dataPagamento;
        this.formaPagamentoId = formaPagamentoId;
        this.formaPagamento = formaPagamento;
    }

    public PagamentoResponse() {
    }


    public static PagamentoResponse converter(Pagamento p) {
        return new PagamentoResponse(
                p.getId(),
                p.getValor(),
                p.isPago(),
                p.getDataPagamento(),
                p.getFormaPagamento() != null ? p.getFormaPagamento().getIdFormaPagamento() : null,
                p.getFormaPagamento() != null ? p.getFormaPagamento().getTitulo() : null
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Integer formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
