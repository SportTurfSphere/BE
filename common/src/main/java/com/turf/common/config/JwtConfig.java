package com.turf.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.refresh.token.expiration.time}")
    private int refreshTokenExpTime;

    @Value("${jwt.access.token.expiration.time}")
    private int accessTokenExpTime;

}
