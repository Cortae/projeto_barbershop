package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.StatusAgendamento;

public interface StatusAgendamentoRepository extends JpaRepository<StatusAgendamento, Integer> {

    boolean existsByTituloContainingIgnoreCase(String titulo);

    StatusAgendamento findByTituloContainingIgnoreCase(String titulo);
}
