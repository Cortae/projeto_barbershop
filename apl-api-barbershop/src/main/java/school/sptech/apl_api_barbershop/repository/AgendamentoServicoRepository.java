package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;

import java.util.List;

public interface AgendamentoServicoRepository extends JpaRepository<AgendamentoServico, Integer> {

    boolean existsByAgendamentoIdAgendamento(Integer id);

    void deleteByAgendamentoIdAgendamento(Integer id);

    List<AgendamentoServico> findByAgendamentoIdAgendamento(Integer id);
}
