package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan( { "com.example" } )
public class ServerGatewayApplication {

    public static void main( String[] args ) {
        SpringApplication.run( ServerGatewayApplication.class, args );
    }

}
