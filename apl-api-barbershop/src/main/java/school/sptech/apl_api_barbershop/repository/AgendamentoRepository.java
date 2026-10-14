package school.sptech.apl_api_barbershop.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Agendamento;

import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    Optional<Agendamento> findByPagamentoId(Integer id);
}
