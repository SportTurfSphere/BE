package com.turf.gateway.config.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

import static com.turf.gateway.constants.AppConstants.*;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            DOCS_API, BEARER_TOKEN, AUTHORISATION_TOKEN, DEFAULT_API_DOCS_URL,
            REGISTER, LOGIN, REFRESH_TOKEN,VERIFY_REGISTER,PASSWORD_RESET_VERIFY,REQUEST_PASSWORD_RESET,
            VALIDATE_LINK, RSA_KEY
    );

    Predicate<ServerHttpRequest> isSecured = request -> {
        for (String uri : openApiEndpoints) {
            if (request.getURI().getPath().contains(uri))
                return false; // Not secured if the request matches any openApiEndpoint
        }
        return true; // Secured if the request does not match any openApiEndpoint
    };
}