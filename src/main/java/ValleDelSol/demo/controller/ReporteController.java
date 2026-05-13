package ValleDelSol.demo.controller;

import ValleDelSol.demo.domain.reporte.CrearReporteDTO;
import ValleDelSol.demo.domain.reporte.EstadoReporteDTO;
import ValleDelSol.demo.domain.reporte.ReporteIncendio;
import ValleDelSol.demo.domain.reporte.ReporteIncendioRepository;
import ValleDelSol.demo.domain.reporte.ReporteResponseDTO;
import ValleDelSol.demo.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteIncendioRepository reporteRepository;

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crearReporte(
            @RequestBody @Valid CrearReporteDTO datosReporte,
            Authentication authentication
    ) {
        // Extraemos el usuario autenticado del token
        User usuarioCreador = (User) authentication.getPrincipal();

        // Creamos la entidad
        ReporteIncendio nuevoReporte = new ReporteIncendio(datosReporte, usuarioCreador);

        // Guardamos en la base de datos
        reporteRepository.save(nuevoReporte);

        // Devolvemos la respuesta
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReporteResponseDTO(nuevoReporte));
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listarReportes() {
        // Buscamos todos los reportes ordenados del más reciente al más antiguo
        List<ReporteIncendio> reportes = reporteRepository.findAllByOrderByFechaDescHoraDesc();
        
        // Convertimos a DTOs
        List<ReporteResponseDTO> response = reportes.stream()
                .map(ReporteResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Solo un ADMIN o FUNCIONARIO puede modificar el estado u otros datos de la alerta
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ReporteResponseDTO> actualizarEstadoReporte(@PathVariable Long id, @RequestBody @Valid EstadoReporteDTO datos) {
        Optional<ReporteIncendio> reporteOptional = reporteRepository.findById(id);

        if (reporteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReporteIncendio reporte = reporteOptional.get();
        reporte.actualizarEstado(datos.estado()); 
        reporteRepository.save(reporte);

        return ResponseEntity.ok(new ReporteResponseDTO(reporte));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        Optional<ReporteIncendio> reporteOptional = reporteRepository.findById(id);

        if (reporteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        reporteRepository.deleteById(id);
        
        // Retornamos 204 No Content para indicar que se borró exitosamente sin devolver un cuerpo
        return ResponseEntity.noContent().build();
    }
}
