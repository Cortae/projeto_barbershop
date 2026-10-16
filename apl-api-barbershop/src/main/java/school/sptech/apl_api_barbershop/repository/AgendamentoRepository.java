package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.apl_api_barbershop.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    Optional<Agendamento> findByPagamentoId(Integer id);

    List<Agendamento> findAllByBarbeiro_IdOrderByHorarioInicioAsc(Integer id);

    List<Agendamento> findByHorarioInicioBetweenOrderByHorarioInicioAsc(LocalDateTime inicio, LocalDateTime fim);

    @Query("""
    SELECT COUNT(a) > 0
    FROM Agendamento a
    WHERE a.barbeiro.id = :barbeiroId
      AND (:agendamentoId IS NULL OR a.idAgendamento <> :agendamentoId)
      AND a.horarioInicio < :fim
      AND a.horarioFim > :inicio
      AND a.statusAgendamento.titulo NOT IN ('CANCELADO', 'CONCLUIDO', 'NAO_COMPARECEU')
""")
    boolean existeConflitoHorario(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("barbeiroId") Integer barbeiroId,
            @Param("agendamentoId") Integer agendamentoId
    );

    List<Agendamento> findByCliente_IdAndStatusAgendamento_TituloIgnoreCaseOrderByHorarioInicioDesc(
            Integer id,
            String status
    );
}
