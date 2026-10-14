package school.sptech.apl_api_barbershop.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    Optional<Agendamento> findByPagamentoId(Integer id);

    List<Agendamento> findAllByBarbeiro(Integer id);

    List<Agendamento> findByHorarioInicioBetweenOrderByHorarioInicioAsc(LocalDateTime inicio, LocalDateTime fim);

    Boolean existsAgendamentosByHorarioInicioAndBarbeiroId(LocalDateTime horario, Integer id);

    List<Agendamento> findByClienteId(Integer id);
}
