package ValleDelSol.demo.domain.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Ejemplo de método: Encontrar alertas creadas por un usuario en particular
    List<Alert> findByUsuarioId(Long usuarioId);
}
