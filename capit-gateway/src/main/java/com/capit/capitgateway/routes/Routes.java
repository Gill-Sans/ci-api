package com.capit.capitgateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private String getServiceUrl(String serviceName, int port) {
        boolean isLocal = "local".equals(activeProfile);
        return isLocal ? "http://localhost:" + port : "http://" + serviceName + ":" + port;
    }

    private String getWsServiceUrl(String serviceName, int port) {
        return getServiceUrl(serviceName, port).replaceFirst("^http", "ws");
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("ws_checkins", r -> r.path("/api/interactions/ws/checkins")
                    .and().header("Upgrade", "WebSocket")
                    .filters(f -> f.addRequestHeader("X-Gateway-Auth", "true"))
                    .uri(getWsServiceUrl("capit-interactions", 8081)))
            .route("interaction_service", r -> r.path("/api/interactions/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Auth", "true"))
                .uri(getServiceUrl("capit-interactions", 8081)))

            .route("schedule_service", r -> r.path("/api/schedule/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Auth", "true"))
                .uri(getServiceUrl("capit-schedule", 8082)))

            .route("user_service", r -> r.path("/api/users/**")
                .filters(f -> f.addRequestHeader("X-Gateway-Auth", "true"))
                .uri(getServiceUrl("capit-users", 8080)))

            .build();
    }
}
