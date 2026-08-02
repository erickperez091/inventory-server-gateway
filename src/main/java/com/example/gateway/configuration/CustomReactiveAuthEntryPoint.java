package com.example.gateway.configuration;

import com.example.common.entity.dto.AuthErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class CustomReactiveAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static AuthErrorResponse getAuthErrorResponse(AuthenticationException ex) {
        AuthErrorResponse authErrorResponse = new AuthErrorResponse("You are not authenticated", "AUTH-401-NON-AUTHENTICATED");

        if (ex instanceof InsufficientAuthenticationException) {
            authErrorResponse = new AuthErrorResponse("You must login, not enough authentication", "AUTH-401-INSUFFICIENT-AUTH-GW");
        } else if (ex instanceof BadCredentialsException) {
            authErrorResponse = new AuthErrorResponse("Invalid or expired token", "AUTH-401-BAD-CREDENTIALS-GW");
        } else if (ex instanceof AuthenticationCredentialsNotFoundException) {
            authErrorResponse = new AuthErrorResponse("Missing Bearer token", "AUTH-401-MISSING-TOKEN-GW");
        }
        return authErrorResponse;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        AuthErrorResponse authErrorResponse = getAuthErrorResponse(ex);

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        byte[] responseBytes;
        try {
            responseBytes = objectMapper.writeValueAsBytes(authErrorResponse);
        } catch (JsonProcessingException e) {
            responseBytes = "{\"message\": \"Authentication error\"}".getBytes(StandardCharsets.UTF_8);
        }
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

