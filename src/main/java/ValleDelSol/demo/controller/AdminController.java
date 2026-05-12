package ValleDelSol.demo.controller;

import ValleDelSol.demo.domain.user.ActualizarRolDTO;
import ValleDelSol.demo.domain.user.User;
import ValleDelSol.demo.domain.user.UserRepository;
import ValleDelSol.demo.domain.user.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Solo un ADMIN puede cambiar el rol de otro usuario
    @PutMapping("/usuarios/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> cambiarRol(@PathVariable Long id, @RequestBody @Valid ActualizarRolDTO datos) {
        Optional<User> usuarioOptional = userRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User usuario = usuarioOptional.get();
        usuario.actualizarRol(datos.nuevoRol());
        userRepository.save(usuario);

        return ResponseEntity.ok(new UserResponseDTO(usuario));
    }
}
