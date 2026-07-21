package com.example.gateway.filter;

import com.example.gateway.configuration.CustomReactiveAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@Component
public class JwtAuthGatewayFilterConfig {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final CustomReactiveAuthEntryPoint authEntryPoint;


    @Bean
    public AuthenticationWebFilter jwtAuthWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager);

        filter.setServerAuthenticationConverter(exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.empty();
            }
            String token = authHeader.substring(7);
            return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
        });

        ServerWebExchangeMatcher matcher = exchange -> {
            String path = exchange.getRequest().getPath().toString();
            if (path.startsWith("/api/auth/") || path.equals("/ping") || path.startsWith("/actuator")) {
                return ServerWebExchangeMatcher.MatchResult.notMatch();
            }
            if (path.startsWith("/api/")) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        };

        filter.setRequiresAuthenticationMatcher(matcher);
        filter.setAuthenticationFailureHandler(
                new ServerAuthenticationEntryPointFailureHandler(authEntryPoint)
        );

        return filter;
    }
}
