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

    @Query("""
    SELECT COUNT(a) > 0
    FROM Agendamento a
    WHERE a.barbeiro.id = :barbeiroId
      AND a.idAgendamento <> :agendamentoId
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

    List<Agendamento> findByClienteId(Integer id);

    @Query("""
        SELECT a FROM Agendamento a
        JOIN a.pagamento p ON p.pago = false
        JOIN a.statusAgendamento s ON s.titulo = 'CONCLUIDO'
        ORDER BY a.horarioInicio
        """)
    List<Agendamento> buscarPagamentosPendentes();
}
