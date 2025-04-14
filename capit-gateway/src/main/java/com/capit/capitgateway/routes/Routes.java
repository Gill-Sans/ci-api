package com.capit.capitgateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestHeader;

@Configuration
public class Routes {
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private String getServiceUrl(String serviceName, int port) {
        boolean isLocalProfile = "local".equals(activeProfile);
        return isLocalProfile ? "http://localhost:" + port : "http://" + serviceName + ":" + port;
    }

    @Bean
    public RouterFunction<ServerResponse> InteractionServiceRoute() {
        return GatewayRouterFunctions.route("interaction_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/interaction/**"), 
                       HandlerFunctions.http(getServiceUrl("capit-interactions", 8081)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> ScheduleServiceRoute() {
        return GatewayRouterFunctions.route("schedule_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/schedule/**"), 
                       HandlerFunctions.http(getServiceUrl("capit-schedule", 8082)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> UserServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/users/**"), 
                       HandlerFunctions.http(getServiceUrl("capit-users", 8080)))
                .build();
    }
}
