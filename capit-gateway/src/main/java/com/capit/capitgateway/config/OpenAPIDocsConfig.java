package com.capit.capitgateway.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIDocsConfig {

    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Capit Gateway API")
                                .version("1.0")
                                .description("API documentation for Capit gateway service")
                                .license(new License().name("MIT License"))
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("You can refer to the gateway service documentation on confluence")
                                .url("https://capit-internship.atlassian.net/wiki/home")
                );
    }
}