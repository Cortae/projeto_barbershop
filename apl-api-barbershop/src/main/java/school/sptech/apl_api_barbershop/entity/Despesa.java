package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDespesa;
    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private Usuario usuario;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataDespesa;

    public Despesa(Integer idDespesa, Usuario usuario, String descricao, BigDecimal valor, LocalDate dataDespesa) {
        this.idDespesa = idDespesa;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDespesa = dataDespesa;
    }

    public Despesa() {
    }

    public Integer getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(Integer idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
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
