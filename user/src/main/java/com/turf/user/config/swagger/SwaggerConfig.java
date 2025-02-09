package com.turf.user.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Log4j2
@Configuration
class SwaggerConfig {
    @Value("${server.servlet.context-path}")
    public String contextPath;
    private static final String BEARER_TOKEN = "Bearer";
    public static final String AUTHORISATION_TOKEN = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .openapi("3.0.3")
                .addServersItem(new Server().url(contextPath))
                .components(new Components()
                        .addSecuritySchemes(AUTHORISATION_TOKEN, bearerTokenSecurityScheme()))
                .security(Collections.singletonList(new SecurityRequirement().addList(AUTHORISATION_TOKEN)));
    }

    public SecurityScheme bearerTokenSecurityScheme() {
        return new SecurityScheme()
                .scheme(BEARER_TOKEN)
                .name(AUTHORISATION_TOKEN) // authorisation-token
                .description("Authorization Header Required only for the Private APIs")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.HTTP);
    }
}
