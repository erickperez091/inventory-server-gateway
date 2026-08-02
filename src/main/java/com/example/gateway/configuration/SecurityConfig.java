package com.example.gateway.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableReactiveMethodSecurity
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthenticationWebFilter jwtAuthWebFilter) {
        jwtAuthWebFilter.setRequiresAuthenticationMatcher(
                new NegatedServerWebExchangeMatcher(
                        ServerWebExchangeMatchers.pathMatchers(HttpMethod.OPTIONS, "/**")
                )
        );


        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange ->
                        exchange
                                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .pathMatchers("/api/auth/**", "/ping", "/api/*/actuator/health/**").permitAll()
                                .anyExchange().authenticated())
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new CustomReactiveAuthEntryPoint()))
                .addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
