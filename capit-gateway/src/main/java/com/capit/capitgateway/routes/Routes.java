package com.capit.capitgateway.routes;

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

    @Bean
    public RouterFunction<ServerResponse> InteractionServiceRoute() {
        return GatewayRouterFunctions.route("interaction_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/interaction/**"), HandlerFunctions.http("http://capit-interactions:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> ScheduleServiceRoute() {
        return GatewayRouterFunctions.route("schedule_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/schedule/**"), HandlerFunctions.http("http://capit-schedule:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> UserServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                .before(addRequestHeader("X-Gateway-Auth", "true"))
                .route(RequestPredicates.path("/api/user/**"), HandlerFunctions.http("http://localhost:8080"))
                .build();
    }
}
