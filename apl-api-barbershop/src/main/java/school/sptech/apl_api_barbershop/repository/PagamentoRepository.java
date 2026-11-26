package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
