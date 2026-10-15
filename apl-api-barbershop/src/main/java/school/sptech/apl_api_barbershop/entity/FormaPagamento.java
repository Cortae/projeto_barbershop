package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento {
    @Id
    @Column(name = "id_forma_pagamento")
    private Integer idFormaPagamento;

    @NotBlank
    private String titulo;

    public FormaPagamento(Integer idFormaPagamento, String titulo) {
        this.idFormaPagamento = idFormaPagamento;
        this.titulo = titulo;
    }

    public FormaPagamento() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(Integer idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }
}
