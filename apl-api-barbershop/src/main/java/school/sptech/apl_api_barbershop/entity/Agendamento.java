package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAgendamento;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "fk_status_agendamento")
    private StatusAgendamento statusAgendamento;

    @ManyToOne
    @JoinColumn(name = "barbeiro")
    private Usuario barbeiro;

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "fk_pagamento")
    private Pagamento pagamento;

    public Agendamento(){}

    public Agendamento(Integer idAgendamento, LocalDateTime horarioInicio, LocalDateTime horarioFim, String titulo, StatusAgendamento statusAgendamento, Usuario barbeiro, Cliente cliente, Pagamento pagamento) {
        this.idAgendamento = idAgendamento;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.titulo = titulo;
        this.statusAgendamento = statusAgendamento;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.pagamento = pagamento;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public LocalDateTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalDateTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDateTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalDateTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public StatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public Usuario getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Usuario barbeiro) {
        this.barbeiro = barbeiro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
