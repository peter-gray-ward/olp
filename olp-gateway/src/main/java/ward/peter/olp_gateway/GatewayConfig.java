package ward.peter.olp_gateway;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RateLimiter coursesRateLimiter() {
        return RateLimiter.of("coursesRateLimiter",
                RateLimiterConfig.custom()
                        .limitForPeriod(5) // Max 5 requests
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .timeoutDuration(Duration.ofMillis(0))
                        .build());
    }
}
