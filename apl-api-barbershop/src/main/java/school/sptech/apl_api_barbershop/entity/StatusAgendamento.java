package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class StatusAgendamento {

    @Id
    private Integer idStatusAgendamento;
    private String titulo;

    public StatusAgendamento(){}

    public StatusAgendamento(Integer idStatusAgendamento, String titulo) {
        this.idStatusAgendamento = idStatusAgendamento;
        this.titulo = titulo;
    }

    public Integer getIdStatusAgendamento() {
        return idStatusAgendamento;
    }

    public String getTitulo() {
        return titulo;
    }

}

