package com.servicedesk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String bearerScheme = "bearerAuth";
        String orgIdScheme = "X-Org-Id";

        Components components = new Components()
                .addSecuritySchemes(orgIdScheme,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Org-Id"))
                .addSecuritySchemes(bearerScheme,
                        new SecurityScheme()
                                .name(bearerScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));

        return new OpenAPI()
                .openapi("3.0.1")
                .info(new Info().title("ServiceDesk API")
                        .version("v1"))
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(orgIdScheme))
                .addSecurityItem(new SecurityRequirement().addList(bearerScheme));
    }
}
