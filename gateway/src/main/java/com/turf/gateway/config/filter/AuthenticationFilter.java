package com.turf.gateway.config.filter;

import com.turf.common.dto.GenericResponse;
import com.turf.common.dto.ResultInfo;
import com.turf.common.dto.TokenInfo;
import com.turf.common.util.ObjectMapperUtil;
import com.turf.gateway.util.TokenValidationUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

import static com.turf.gateway.constants.AppConstants.*;


@Component
@Log4j2
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private static final String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

    @Autowired
    private RouteValidator validator;

    @Autowired
    private TokenValidationUtil tokenValidationUtil;

    //    @Value("${content.source}")
    String contentSource;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            setResponseHeaders(exchange.getResponse());
            if (validator.isSecured.test(exchange.getRequest())) {
                return handleSecuredRequest(exchange, chain);
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleSecuredRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        log.info("PATH:{} Headers:{}",request.getURI().getPath(), headers);
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            log.error("Required authorize token");
            return customExchange(exchange, AUTH_HEADER_MISSING);
        }
        String token = retrieveToken(headers);
        try {
            if (!Objects.requireNonNull(tokenValidationUtil).isAccessTokenPresent(token)) {
                log.error("Authentication failed from auth service");
                return customExchange(exchange, AUTH_HEADER_INVALID);
            }
        } catch (Exception e) {
            log.error("Authentication error : {}", e.getMessage());
            return customExchange(exchange, e.getMessage());
        }
        setSecureHeaders(exchange, tokenValidationUtil.fetchTokenInfo(token));
        return chain.filter(exchange);
    }

    private String retrieveToken(HttpHeaders headers){
        try {
            String token = headers.get(HttpHeaders.AUTHORIZATION).get(0);
            if (token != null && token.startsWith(BEARER)) {
                token = token.substring(BEARER.length());
            }
            return token;
        }
        catch (NullPointerException npe){
            return null;
        }
    }

    private Mono<Void> customExchange(ServerWebExchange exchange, String error)  {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        setCORSResHeaders(response);
        GenericResponse errorResponse = GenericResponse.builder().resultInfo(unAuthorizedResultInfo(error)).build();
        DataBuffer buffer = null;
        try {
            buffer = exchange.getResponse().bufferFactory().wrap(ObjectMapperUtil.writeValueAsBytes(errorResponse));
        } catch (IOException e) {
            log.error("Error while parsing error response:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private void setCORSResHeaders(ServerHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
    }

    private void setSecureHeaders(ServerWebExchange exchange, TokenInfo token) {
        if (Objects.nonNull(token)) {
            exchange.getRequest().mutate()
                    .header(LOGGEDIN_USER_NAME, token.getLoggedInUserName())
                    .header(LOGGEDIN_USER_ID, String.valueOf(token.getLoggedInUserId()))
                    .header(LOGGEDIN_USER_UUID, token.getLoggedInUserUUID())
                    .header(LOGGEDIN_NAME, token.getName())
                    .header(LOGGEDIN_USER_TYPE, token.getUserType())
                    .header(REFERER_POLICY, REFERER_POLICY_VALUE)
                    .header(X_FRAME_OPTIONS, X_FRAME_OPTIONS_VALUE)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class Config {
        String name;
    }

    private ResultInfo unAuthorizedResultInfo(String error) {
        return new ResultInfo(UNAUTHORIZED_CODE, UNAUTHORIZED_MESSAGE, error, UNAUTHORIZED_STATUS);
    }

    private void setResponseHeaders(ServerHttpResponse response) {
        response.getHeaders().add(CONTENT_SECURITY_POLICY_HEADER, buildContentSecurityPolicy().toString());
    }

    private StringBuilder buildContentSecurityPolicy() {
        return new StringBuilder()
                .append("default-src 'self'; ")
                .append("script-src 'self' ").append(contentSource).append(";")
                .append("style-src 'self' ").append(contentSource).append(";");
    }
}
