package com.example.gateway.configuration;

import com.example.common.utilities.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

    private final JwtUtils jwtUtils;

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            if (!this.jwtUtils.isTokenValid(token)) {
                return Mono.error(new BadCredentialsException("Invalid or expired token"));
            }
            String username = jwtUtils.getUsername(token);
            return Mono.just(new UsernamePasswordAuthenticationToken(username, token, List.of()));
        };
    }
}
