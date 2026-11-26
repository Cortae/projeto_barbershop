package school.sptech.apl_api_barbershop.dto.agendamento;

import school.sptech.apl_api_barbershop.dto.pagamento.PagamentoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoResponse {
    private Integer idAgendamento;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private String titulo;
    private String status;
    private Integer barbeiroId;
    private ClienteResponse cliente;
    private List<ServicoResponse> servicos;
    private PagamentoResponse pagamento;

    public AgendamentoResponse() {
    }

    public AgendamentoResponse(Integer idAgendamento, LocalDateTime horarioInicio, LocalDateTime horarioFim,
                               String titulo, String status, Integer barbeiroId, ClienteResponse cliente,
                               List<ServicoResponse> servicos, PagamentoResponse pagamento) {
        this.idAgendamento = idAgendamento;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.titulo = titulo;
        this.status = status;
        this.barbeiroId = barbeiroId;
        this.cliente = cliente;
        this.servicos = servicos;
        this.pagamento = pagamento;
    }

    public Integer getIdAgendamento() { return idAgendamento; }
    public void setIdAgendamento(Integer idAgendamento) { this.idAgendamento = idAgendamento; }
    public LocalDateTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalDateTime horarioInicio) { this.horarioInicio = horarioInicio; }
    public LocalDateTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalDateTime horarioFim) { this.horarioFim = horarioFim; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getBarbeiroId() { return barbeiroId; }
    public void setBarbeiroId(Integer barbeiroId) { this.barbeiroId = barbeiroId; }
    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }
    public List<ServicoResponse> getServicos() { return servicos; }
    public void setServicos(List<ServicoResponse> servicos) { this.servicos = servicos; }
    public PagamentoResponse getPagamento() { return pagamento; }
    public void setPagamento(PagamentoResponse pagamento) { this.pagamento = pagamento; }

    public static class ClienteResponse {
        private Integer id;
        private String nome;
        private String apelido;
        private String telefone;
        private LocalDate nascimento;

        public ClienteResponse() {
        }

        public ClienteResponse(Integer id, String nome, String apelido, String telefone, LocalDate nascimento) {
            this.id = id;
            this.nome = nome;
            this.apelido = apelido;
            this.telefone = telefone;
            this.nascimento = nascimento;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getApelido() { return apelido; }
        public void setApelido(String apelido) { this.apelido = apelido; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public LocalDate getNascimento() { return nascimento; }
        public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }
    }

    public static class ServicoResponse {
        private Integer id;
        private String titulo;
        private BigDecimal valorUnitario;
        private Integer duracao;

        public ServicoResponse() {
        }

        public ServicoResponse(Integer id, String titulo, BigDecimal valorUnitario, Integer duracao) {
            this.id = id;
            this.titulo = titulo;
            this.valorUnitario = valorUnitario;
            this.duracao = duracao;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
        public Integer getDuracao() { return duracao; }
        public void setDuracao(Integer duracao) { this.duracao = duracao; }
    }
}
