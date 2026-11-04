package Application.adapters.security;

import Application.domain.enums.UserStatus;
import Application.domain.model.user.User;
import Application.domain.ports.out.JwtServicePort;
import Application.domain.ports.out.UserRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// valida el JWT de cada request y arma la Authentication de Spring
// Security a partir de EL USUARIO REAL en la base de datos, no solo del
// contenido del token (asi lo pide Rest-security-cors.md: "carga el
// usuario por sub via UserRepositoryPort, rechaza si no existe o status
// != ACTIVE"). esto es a proposito mas estricto que confiar ciegamente
// en los claims: si al usuario lo bloquearon despues de emitido el
// token, este filtro lo rechaza en la siguiente request de todas formas.
//
// si el token falta, es invalido, o el usuario no existe/esta
// bloqueado, este filtro simplemente NO pone ninguna Authentication (la
// request sigue como anonima) y deja que SecurityConfig decida que
// pasa: una ruta publica sigue adelante igual, una protegida termina en
// el AuthenticationEntryPoint (401)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServicePort jwtServicePort;
    private final UserRepositoryPort userRepositoryPort;

    public JwtAuthenticationFilter(JwtServicePort jwtServicePort, UserRepositoryPort userRepositoryPort) {
        this.jwtServicePort = jwtServicePort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {
        extractToken(request)
                .filter(jwtServicePort::isValid)
                .map(jwtServicePort::getUserIdFromToken)
                .flatMap(userRepositoryPort::findById)
                .filter(user -> user.getStatus() == UserStatus.ACTIVE)
                .ifPresent(this::authenticate);

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(header.substring("Bearer ".length()));
    }

    private void authenticate(User user) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + UserRoles.of(user)));
        // principal = el id del usuario (Long): es lo que los controllers
        // necesitan para delegar en los puertos de entrada (buyerId,
        // sellerId, etc, ver adapters/useCases)
        var authentication = new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
