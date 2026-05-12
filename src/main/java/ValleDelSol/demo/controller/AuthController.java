package ValleDelSol.demo.controller;

import ValleDelSol.demo.domain.user.*;
import ValleDelSol.demo.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody @Valid AuthenticationUserDTO authenticationUserDTO) {
        // Estandarizar el correo a minúsculas
        String emailToLowerCase = authenticationUserDTO.email().toLowerCase();
        
        Authentication authToken = new UsernamePasswordAuthenticationToken(emailToLowerCase, authenticationUserDTO.password());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generateToken((User) usuarioAutenticado.getPrincipal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Inicio de sesión exitoso");
        response.put("token", JWTtoken);
        response.put("user", new UserResponseDTO((User) usuarioAutenticado.getPrincipal()));
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        // Estandarizar el correo a minúsculas
        String emailToLowerCase = registerUserDTO.email().toLowerCase();
        
        if (userRepository.existsByEmail(emailToLowerCase)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "El correo electrónico ya está registrado.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        String encodedPassword = passwordEncoder.encode(registerUserDTO.password());
        User newUser = new User(registerUserDTO.nombre(), emailToLowerCase, encodedPassword);
        userRepository.save(newUser);

        var JWTtoken = tokenService.generateToken(newUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente");
        response.put("user", new UserResponseDTO(newUser));
        response.put("token", JWTtoken);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserResponseDTO(user));
    }
}
