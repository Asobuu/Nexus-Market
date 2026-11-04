package Application.adapters.rest;

import Application.adapters.rest.dto.request.LoginRequest;
import Application.adapters.rest.dto.request.RegisterBuyerRequest;
import Application.adapters.rest.dto.response.AuthTokenResponse;
import Application.adapters.rest.dto.response.BuyerResponse;
import Application.domain.model.AuthToken;
import Application.domain.model.user.Buyer;
import Application.domain.ports.in.PublicAccessPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// unico controller sin sesion: registrar un comprador y hacer login
// (ver Api-rest-endpoints.md: "/api/auth/**" es publica)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PublicAccessPort publicAccessPort;

    public AuthController(PublicAccessPort publicAccessPort) {
        this.publicAccessPort = publicAccessPort;
    }

    @PostMapping("/register")
    public ResponseEntity<BuyerResponse> register(@Valid @RequestBody RegisterBuyerRequest request) {
        Buyer buyer = publicAccessPort.registerBuyer(request.getFullName(), request.getEmail(),
                request.getRawPassword(), request.getAddress().toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthToken token = publicAccessPort.login(request.getEmail(), request.getRawPassword());
        return ResponseEntity.ok(AuthTokenResponse.from(token));
    }
}
