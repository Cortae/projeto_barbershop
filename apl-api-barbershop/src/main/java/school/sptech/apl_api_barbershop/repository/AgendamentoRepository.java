package school.sptech.apl_api_barbershop.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.apl_api_barbershop.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    Optional<Agendamento> findByPagamentoId(Integer id);

    List<Agendamento> findAllByBarbeiro(Integer id);

    List<Agendamento> findByHorarioInicioBetweenOrderByHorarioInicioAsc(LocalDateTime inicio, LocalDateTime fim);

    @Query("""
    SELECT COUNT(a) > 0
    FROM Agendamento a
    WHERE a.horarioInicio = :horario
      AND a.barbeiro.id = :barbeiroId
      AND a.statusAgendamento.titulo NOT IN ('CANCELADO', 'CONCLUIDO', 'NAO_COMPARECEU')
""")
    boolean existeAgendamentoAtivo(
            @Param("horario") LocalDateTime horario,
            @Param("barbeiroId") Integer barbeiroId
    );

    List<Agendamento> findByClienteId(Integer id);
}
