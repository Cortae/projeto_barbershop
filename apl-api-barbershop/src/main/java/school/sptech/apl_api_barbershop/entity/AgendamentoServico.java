package school.sptech.apl_api_barbershop.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class AgendamentoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAgendamentoServicos;

    @ManyToOne
    @JoinColumn(name = "fk_agendamento")
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "fk_servico")
    private Servico servico;

    private BigDecimal valorUnitario;
    private Integer duracao;

    public AgendamentoServico(){}

    public AgendamentoServico(Integer idAgendamentoServicos, Agendamento agendamento, Servico servico, BigDecimal valorUnitario, Integer duracao) {
        this.idAgendamentoServicos = idAgendamentoServicos;
        this.agendamento = agendamento;
        this.servico = servico;
        this.valorUnitario = valorUnitario;
        this.duracao = duracao;
    }

    public Integer getIdAgendamentoServicos() {
        return idAgendamentoServicos;
    }

    public void setIdAgendamentoServicos(Integer idAgendamentoServicos) {
        this.idAgendamentoServicos = idAgendamentoServicos;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }
}
