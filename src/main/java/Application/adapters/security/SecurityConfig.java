package Application.adapters.security;

import Application.adapters.rest.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// cablea todo lo que pide Rest-security-cors.md: sin sesion (stateless),
// JwtAuthenticationFilter antes del filtro de usuario/clave de Spring,
// autorizacion por rol segun la tabla de Api-rest-endpoints.md, y CORS.
// POST /api/orders/{id}/pay y POST /api/returns/{id}/complete-refund no
// estan en esa tabla (quedo incompleta: BuyerPort.payOrder y
// AdministratorPort.completeRefund existen en el dominio pero sin ruta
// asignada), asi que se agregan aca con el mismo criterio que el resto
// de cada rol
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // propio, no inyectado: por alguna razon con esta combinacion de
    // dependencias Spring no esta exponiendo un bean ObjectMapper
    // autoconfigurado para inyectar aca, asi que se arma uno manual con
    // JavaTimeModule (necesario para serializar el LocalDateTime de
    // ErrorResponse.timestamp) y fechas en ISO-8601, no como timestamp numerico
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        // publico (ver Api-rest-endpoints.md: "/api/auth/** y GET /api/products")
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()

                        // seller
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/products/*/variants").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/status").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/products/mine").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/inventory/*/receipt").hasRole("SELLER")

                        // buyer
                        .requestMatchers(HttpMethod.POST, "/api/cart/items").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/cart").hasRole("BUYER")
                        .requestMatchers(HttpMethod.POST, "/api/cart/checkout").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/mine").hasRole("BUYER")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/pay").hasRole("BUYER")
                        .requestMatchers(HttpMethod.POST, "/api/returns").hasRole("BUYER")

                        // logistics operator
                        .requestMatchers(HttpMethod.POST, "/api/shipments/*/assign-self").hasRole("LOGISTICS_OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/shipments/*/ship").hasRole("LOGISTICS_OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/shipments/*/deliver").hasRole("LOGISTICS_OPERATOR")

                        // administrator
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/returns/*/approve").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/returns/*/reject").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/returns/*/complete-refund").hasRole("ADMINISTRATOR")

                        // supervisor
                        .requestMatchers(HttpMethod.GET, "/api/reports/**").hasRole("SUPERVISOR")

                        // cualquier otra ruta: al menos autenticado, nunca abierta por omision
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        // sin token o token invalido en una ruta protegida
                        .authenticationEntryPoint((request, response, authException) ->
                                writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED",
                                        "se necesita un token valido para acceder a este recurso"))
                        // token valido pero el rol no alcanza
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeError(response, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN",
                                        "no tiene permiso para acceder a este recurso")));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // en desarrollo; ajustar segun el frontend que se use (ver Rest-security-cors.md)
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Request-Id"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    private void writeError(HttpServletResponse response, int status, String error, String message)
            throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(status, error, message)));
    }
}
