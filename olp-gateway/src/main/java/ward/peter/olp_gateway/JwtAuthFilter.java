package ward.peter.olp_gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter implements WebFilter {

    private static final String SECRET_KEY = "yourSuperSecretKeyForJWTValidation"; // Same key as the Auth Service

    private static final List<String> PUBLIC_PATHS = List.of(
        "/auth/login",
        "/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        System.out.println("\n\tFiltering " + path + " with JWT validation...");

        if (PUBLIC_PATHS.contains(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        System.out.println("\n\t\tAuth header: " + authHeader);

        String token = authHeader.substring(7);

        System.out.println("\n\t\t\tToken: " + token);
        try {
            Claims claims = validateToken(token);

            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                System.out.println("\n\t\t\t\t" + entry.getKey() + " : " + entry.getValue());
            }

            String username = claims.getSubject();

            String roles = claims.get("roles", String.class);
            if (roles == null) {
                roles = "USER"; // Default role (change based on your system)
            }

            // Convert the single role string into a list of GrantedAuthority objects
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roles));

            // Create authentication object
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);


            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Roles", roles)
                        .build())
                .build();

            System.out.println("\n\tModified Headers:");
            mutatedExchange.getRequest().getHeaders().forEach((k, v) -> System.out.println("\t\t" + k + ": " + v));





            return chain.filter(mutatedExchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\t UNAUTHORIZED");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
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
