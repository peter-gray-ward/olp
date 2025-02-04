package ward.peter.olp_authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtAuthFilter implements ServerAuthenticationConverter {

    private static final String SECRET_KEY = "yourSuperSecretKeyForJWTValidation";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty(); // No authentication, continue with the request
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = validateToken(token);
            String username = claims.getSubject();

            Authentication authentication = new JwtAuthentication(username, token);
            return Mono.just(authentication);
        } catch (Exception e) {
            return Mono.empty(); // Invalid token, do not authenticate
        }
    }

    private Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
