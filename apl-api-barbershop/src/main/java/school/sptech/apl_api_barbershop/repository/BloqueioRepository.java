package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Bloqueio;
import school.sptech.apl_api_barbershop.entity.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {

    List<Bloqueio> findByUsuarioAndInicioBetween(Usuario usuario, LocalDateTime inicio, LocalDateTime fim);

    List<Bloqueio> findByUsuarioIdAndInicioLessThanAndFimGreaterThanOrderByInicioAsc(
            Integer usuarioId,
            LocalDateTime fimPeriodo,
            LocalDateTime inicioPeriodo
    );
}
