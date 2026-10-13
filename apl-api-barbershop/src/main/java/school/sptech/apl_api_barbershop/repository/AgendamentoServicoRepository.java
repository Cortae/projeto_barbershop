package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.AgendamentoServico;

public interface AgendamentoServicoRepository extends JpaRepository<AgendamentoServico, Integer> {
}
