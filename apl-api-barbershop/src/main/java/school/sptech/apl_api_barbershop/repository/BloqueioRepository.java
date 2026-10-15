package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Bloqueio;

import java.time.LocalDateTime;
import java.util.List;

public interface BloqueioRepository extends JpaRepository<Bloqueio, Integer> {

    boolean existsByUsuarioIdAndInicioLessThanAndFimGreaterThan(
            Integer usuarioId,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    List<Bloqueio> findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(
            Integer usuarioId,
            LocalDateTime fimPeriodo,
            LocalDateTime inicioPeriodo
    );
}
