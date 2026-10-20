package school.sptech.apl_api_barbershop.dto.despesa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaRequest {
    @NotNull
    private Integer usuarioId;
    @Size(max = 100)
    @NotBlank
    private String descricao;
    @NotNull
    @Positive
    private BigDecimal valor;
    @NotNull
    private LocalDate dataDespesa;

    public DespesaRequest(Integer usuarioId, String descricao, BigDecimal valor, LocalDate dataDespesa) {
        this.usuarioId = usuarioId;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDespesa = dataDespesa;
    }

    public DespesaRequest() {
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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
