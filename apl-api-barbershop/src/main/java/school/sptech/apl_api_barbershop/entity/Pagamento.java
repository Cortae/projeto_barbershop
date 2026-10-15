package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Integer id;

    @NotNull
    @PositiveOrZero
    private BigDecimal valor;

    @NotNull
    private boolean pago;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @ManyToOne
    @JoinColumn(name =  "fk_forma_pagamento")
    private FormaPagamento formaPagamento;




    public Pagamento(){}

    public Pagamento(Integer id, BigDecimal valor, boolean pago, LocalDateTime dataPagamento, FormaPagamento formaPagamento) {
        this.id = id;
        this.valor = valor;
        this.pago = pago;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
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

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

}
