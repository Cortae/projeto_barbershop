package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Servico;
import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {

    boolean existsByTituloIgnoreCase(String titulo);

    boolean existsByTituloIgnoreCaseAndIdNot(String titulo, Integer id);

    List<Servico> findByAtivoTrue();

    List<Servico> findByAtivoTrueOrderByTituloAsc();

    List<Servico> findByAtivoFalse();
}
