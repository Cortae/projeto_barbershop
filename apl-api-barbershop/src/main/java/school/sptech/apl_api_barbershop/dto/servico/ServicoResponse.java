package school.sptech.apl_api_barbershop.dto.servico;

import school.sptech.apl_api_barbershop.entity.Servico;

import java.math.BigDecimal;

public class ServicoResponse {
    private Integer id;
    private String titulo;
    private BigDecimal valor;
    private Integer duracao;

    public ServicoResponse() {
    }

    public ServicoResponse(Integer id, String titulo, BigDecimal valor, Integer duracao) {
        this.id = id;
        this.titulo = titulo;
        this.valor = valor;
        this.duracao = duracao;
    }

    public static ServicoResponse from(Servico servico) {
        return new ServicoResponse(servico.getId(), servico.getTitulo(),
                servico.getValorAtual(), servico.getDuracaoAtual());
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Integer getDuracao() { return duracao; }
    public void setDuracao(Integer duracao) { this.duracao = duracao; }
}
