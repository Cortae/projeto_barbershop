
package school.sptech.apl_api_barbershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.sptech.apl_api_barbershop.entity.Usuario;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCelular(String celular);

    List<Usuario> findByTipoUsuario_TituloIgnoreCase(String titulo);
}
