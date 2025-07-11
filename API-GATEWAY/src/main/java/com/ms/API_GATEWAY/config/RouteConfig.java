package com.ms.API_GATEWAY.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder)
    {
        return builder
                .routes()

                // AUTH Routes
                .route("auth-service" ,r -> r
                        .path("/auth/**")
                        .or()
                        .path("/admin/**")
                        .uri("lb://AUTH-SERVICE"))

                //RESTAURANT Routes
                .route("restaurant-service" , r->r
                        .path("/restaurant/**")
                        .uri("lb://RESTAURANT-SERVICE"))

                // ORDER Routes
                .route("order-service" , r -> r
                        .path("/order/**")
                        .uri("lb://ORDER-SERVICE"))

                .build();
    }
}
