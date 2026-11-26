package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Integer> {
    java.util.List<FormaPagamento> findAllByOrderByTituloAsc();
}
