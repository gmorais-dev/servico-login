package login.com.login_sistema_frete.dto.response;

public record AuthResponse(
        String token,
        String tipo,
        String email,
        String nome
) {
    public AuthResponse(String token, String email, String nome) {
        this(token, "Bearer", email, nome);
    }
}
