package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Pagamento {

    @Id
    private Integer id;

    public Pagamento(){}

    public Pagamento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
