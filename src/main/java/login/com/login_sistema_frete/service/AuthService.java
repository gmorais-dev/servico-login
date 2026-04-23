package login.com.login_sistema_frete.service;

import login.com.login_sistema_frete.dto.request.CadastroRequest;
import login.com.login_sistema_frete.dto.request.LoginRequest;
import login.com.login_sistema_frete.dto.response.AuthResponse;
import login.com.login_sistema_frete.entity.Usuario;
import login.com.login_sistema_frete.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.gerarToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getNome());
    }

    public AuthResponse cadastrar(CadastroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario);
        return new AuthResponse(token, usuario.getEmail(), usuario.getNome());
    }
}
