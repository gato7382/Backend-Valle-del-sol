package ValleDelSol.demo.domain.reporte;

import jakarta.validation.constraints.NotBlank;

public record EstadoReporteDTO(
        @NotBlank
        String estado
) {
}
