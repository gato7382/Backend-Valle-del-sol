package ValleDelSol.demo.domain.reporte;

import ValleDelSol.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "reportes_incendio")
@Entity(name = "ReporteIncendio")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReporteIncendio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    
    private LocalTime hora;
    
    private String direccion;
    
    private String sector;
    
    private String referencia;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private User usuario;

    public ReporteIncendio(CrearReporteDTO datos, User usuario) {
        this.fecha = datos.fecha();
        this.hora = datos.hora();
        this.direccion = datos.direccion();
        this.sector = datos.sector();
        this.referencia = datos.referencia();
        this.observaciones = datos.observaciones();
        this.estado = "ACTIVO";
        this.usuario = usuario;
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
