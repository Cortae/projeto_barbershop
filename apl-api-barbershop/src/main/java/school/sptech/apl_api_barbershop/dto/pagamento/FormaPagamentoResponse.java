package school.sptech.apl_api_barbershop.dto.pagamento;

import school.sptech.apl_api_barbershop.entity.FormaPagamento;

public class FormaPagamentoResponse {
    private Integer id;
    private String titulo;

    public FormaPagamentoResponse() {
    }

    public FormaPagamentoResponse(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public static FormaPagamentoResponse from(FormaPagamento formaPagamento) {
        return new FormaPagamentoResponse(formaPagamento.getIdFormaPagamento(), formaPagamento.getTitulo());
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
}
