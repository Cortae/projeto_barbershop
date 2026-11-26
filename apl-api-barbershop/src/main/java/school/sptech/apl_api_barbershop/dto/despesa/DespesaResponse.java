package school.sptech.apl_api_barbershop.dto.despesa;

import school.sptech.apl_api_barbershop.dto.UsuarioResponse;


import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaResponse {
    private Integer idDespesa;
    private UsuarioResponse usuario;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataDespesa;

    public DespesaResponse(Integer idDespesa, UsuarioResponse usuario, String descricao, BigDecimal valor, LocalDate dataDespesa) {
        this.idDespesa = idDespesa;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDespesa = dataDespesa;
    }

    public DespesaResponse() {
    }

    public Integer getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(Integer idDespesa) {
        this.idDespesa = idDespesa;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataDespesa() {
        return dataDespesa;
    }

    public void setDataDespesa(LocalDate dataDespesa) {
        this.dataDespesa = dataDespesa;
    }
}

