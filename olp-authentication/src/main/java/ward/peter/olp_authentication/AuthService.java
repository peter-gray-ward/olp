package ward.peter.olp_authentication.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ward.peter.olp_authentication.util.JwtUtil;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String authenticate(String username, String password) {
        // Hash password for comparison
        String encodedPassword = passwordEncoder.encode("enter123"); // Hardcoded for testing

        System.out.println("\n\tAuthenticating " + username + " with pw: " + password + " (" + encodedPassword + ")");

        if ("peter".equals(username.toLowerCase()) && passwordEncoder.matches(password, encodedPassword)) {
            return jwtUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
