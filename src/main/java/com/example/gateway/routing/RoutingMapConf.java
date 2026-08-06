package com.example.gateway.routing;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class RoutingMapConf {

    @Bean
    public RouteLocator configure(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("inventory-service-actuator", route -> route
                        .path("/api/product/actuator/**")
                        .filters(filter -> filter
                                .rewritePath("/api/product/(?<segment>/?.*)", "/$\\{segment}"))
                        .uri("lb://inventory-service"))
                .route("inventory-service", route -> route
                        .path("/api/product/**").or().path("/api/category/**")
                        .filters(filter -> filter
                                .rewritePath("/api/(?<segment>/?.*)", "/$\\{segment}"))
                        .uri("lb://inventory-service"))
                .route("invoice-service", route -> route
                        .path("/api/invoice/**")
                        .filters(filter -> filter
                                .rewritePath("/api/(?<segment>/?.*)", "/$\\{segment}"))
                        .uri("lb://invoice-service"))

                .route("auth-service-actuator", route -> route
                        .path("/api/auth/actuator/**")
                        .filters(filter -> filter
                                .rewritePath("/api/auth/(?<segment>/?.*)", "/$\\{segment}"))
                        .uri("lb://user-service"))
                .route("auth-service", route -> route
                        .path("/api/auth/**", "/api/user/**")
                        .filters(filter -> filter
                                .rewritePath("/api/(?<segment>/?.*)", "/$\\{segment}"))
                        .uri("lb://user-service"))
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }
}
