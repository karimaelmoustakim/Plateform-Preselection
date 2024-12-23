package ma.emsi.gatewayservice.config;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/auth/**")
                        .uri("http://localhost:8081/Service_Authentification")) // Microservice d'authentification
                .route(r -> r.path("/api/applications/**")
                        .uri("http://localhost:8082/Application"))
                .route(r -> r.path("/api/interviewers/**")
                        .uri("http://localhost:8086/interviewers"))
                .route(r -> r.path("/api/rh/interviews/**")
                        .uri("http://localhost:8083/RH"))
                .route(r -> r.path("/api/rh/**")
                        .uri("http://localhost:8083/RH"))

                .build();
    }
    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRoutes(
            ReactiveDiscoveryClient rdc,
            DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }
}

