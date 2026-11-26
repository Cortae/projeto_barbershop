package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Expediente;

import java.util.List;
import java.util.Optional;

public interface ExpedienteRepository extends JpaRepository<Expediente, Integer> {

    List<Expediente> findByUsuarioIdOrderByDiaSemana(Integer usuarioId);

    Optional<Expediente> findByUsuarioIdAndDiaSemana(Integer usuarioId, Integer diaSemana);
}
