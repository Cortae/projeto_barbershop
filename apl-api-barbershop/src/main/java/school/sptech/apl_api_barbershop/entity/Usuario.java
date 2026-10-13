package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {

    @Id
    private Integer id;

    public Usuario(){}

    public Usuario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
