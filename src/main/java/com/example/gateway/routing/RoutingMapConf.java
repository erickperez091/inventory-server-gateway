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
    public RouteLocator configure( RouteLocatorBuilder routeLocatorBuilder ){
        return routeLocatorBuilder.routes()
                .route( "inventory-service", route -> route.path( "/**" ).uri( "lb://inventory-service" ))
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver( DefaultAddressResolverGroup.INSTANCE);
    }
}
