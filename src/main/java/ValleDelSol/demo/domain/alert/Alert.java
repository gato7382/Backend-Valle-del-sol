package ValleDelSol.demo.domain.alert;

import ValleDelSol.demo.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "alertas")
@Entity(name = "Alert")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    
    private String descripcion;
    
    private LocalDateTime fechaCreacion;

    // Relación opcional: Cada alerta puede estar asociada a un usuario que la reportó.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User usuario;

    public Alert(String titulo, String descripcion, User usuario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.fechaCreacion = LocalDateTime.now();
    }
}
