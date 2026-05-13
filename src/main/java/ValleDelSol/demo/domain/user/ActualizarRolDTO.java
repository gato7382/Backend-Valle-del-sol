package ValleDelSol.demo.domain.user;

import jakarta.validation.constraints.NotNull;

public record ActualizarRolDTO(
        @NotNull
        Role nuevoRol
) {
}
