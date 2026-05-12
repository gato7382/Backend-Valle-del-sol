package ValleDelSol.demo.domain.user;

public record UserResponseDTO(
        Long id,
        String nombre,
        String email
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getNombre(), user.getEmail());
    }
}
