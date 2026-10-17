package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByTelefone(String telefone);

    List<Cliente> findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCase(
            String nome,
            String telefone,
            Sort sort
    );

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    List<Cliente> findByTelefoneContaining(String telefone);

    List<Cliente> findAllByOrderByNomeAsc();
}
