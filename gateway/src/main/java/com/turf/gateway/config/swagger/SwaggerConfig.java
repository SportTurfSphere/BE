package com.turf.gateway.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.turf.gateway.constants.AppConstants.AUTHORISATION_TOKEN;
import static com.turf.gateway.constants.AppConstants.BEARER_TOKEN;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
@Log4j2
@Configuration
class SwaggerConfig {
    @Value("${server.servlet.context-path}")
    public String contextPath;
    private final RouteDefinitionLocator locator;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    public SwaggerConfig(RouteDefinitionLocator locator, SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.locator = locator;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing SwaggerConfig");
        List<RouteDefinition> definitions =
                locator.getRouteDefinitions().collectList().block();
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        definitions.stream()
                //   .filter(routeDefinition -> routeDefinition.getId().matches(".*-(?i)service"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId();
                    log.info("Adding service: {}", name);
                    log.info("Predicates: {}", routeDefinition.getPredicates().get(0));
                    String appContext = Optional.ofNullable(routeDefinition.getPredicates().get(0).getArgs().get("_genkey_1")).orElse("");
                    log.info("appContext :{}", appContext );
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl =
                            new AbstractSwaggerUiConfigProperties.SwaggerUrl(
                                    name, appContext+DEFAULT_API_DOCS_URL , name);
                    urls.add(swaggerUrl);
                    log.info("Swagger Url: {}", swaggerUrl);
                });
        swaggerUiConfigProperties.setUrls(urls);
    }

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
