package ward.peter.olp_authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ward.peter.olp_authentication.dto.User;
import ward.peter.olp_authentication.JwtUtil;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRepository repository;

    public AuthService(JwtUtil jwtUtil, AuthRepository authRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.repository = authRepository;
    }

    public String authenticate(String username, String password) {
        Optional<User> userOptional = repository.findByUsername(username);

        if (userOptional.isPresent() == false) {
            return "Username " + username + " does not exist.";
        }


        System.out.println("\n\tAuthenticating " + username + " with password " + password);

        User user = userOptional.get();
        String encodedPassword = passwordEncoder.encode(password);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        }

        return "Invalid credentials";
    }

    public User register(String username, String password) throws Exception {
        if (repository.findByUsername(username).isPresent()) {
            return new User("Username " + username + " is already taken.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = User.builder()
            .username(username)
            .password(hashedPassword)
            .role("USER")
            .build();

        repository.save(user);

        return user;
    }
}
