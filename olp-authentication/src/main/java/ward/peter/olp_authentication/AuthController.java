package ward.peter.olp_authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ward.peter.olp_authentication.service.AuthService;
import ward.peter.olp_authentication.dto.AuthRequest;
import ward.peter.olp_authentication.dto.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("\n\tLOGIN request forwarded to Authentication Service...");
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
