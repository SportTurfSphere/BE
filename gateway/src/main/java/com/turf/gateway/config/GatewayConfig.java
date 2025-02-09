package com.turf.gateway.config;

import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public XForwardedRemoteAddressResolver xForwardedRemoteAddressResolver() {
        return XForwardedRemoteAddressResolver.maxTrustedIndex(1);
    }
}
