package com.example.gateway.configuration;

import com.example.common.utilities.JwtUtils;
import com.example.gateway.filter.JwtAuthGatewayFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
@Order(1)
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    @Bean
    public WebFilter jwtAuthGatewayFilter() {
        return new JwtAuthGatewayFilter(jwtUtils);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().permitAll());
        return http.build();
    }


}
