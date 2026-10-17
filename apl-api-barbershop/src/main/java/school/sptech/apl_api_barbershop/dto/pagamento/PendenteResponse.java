package school.sptech.apl_api_barbershop.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PendenteResponse {
    private Integer pagamentoId;
    private String cliente;
    private String servico;
    private LocalDate data;
    private BigDecimal valor;

    public PendenteResponse(Integer pagamentoId, String cliente, String servico, LocalDate data, BigDecimal valor) {
        this.pagamentoId = pagamentoId;
        this.cliente = cliente;
        this.servico = servico;
        this.data = data;
        this.valor = valor;
    }

    public PendenteResponse() {
    }

    public Integer getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Integer pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
