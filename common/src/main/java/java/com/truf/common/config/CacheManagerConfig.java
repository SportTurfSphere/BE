package java.com.truf.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheManagerConfig {

    @Value("${cache.time.out.min}")
    private Integer cacheTimeOutMin;

    @Value("${cache.max.size}")
    private Integer cacheMaxSize;

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
        return caffeineCacheManager;
    }

    @Bean
    public Cache<String, Object> myCaffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTimeOutMin, TimeUnit.MINUTES)  // Set the expiry time
                .maximumSize(cacheMaxSize)
                .build();
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheTimeOutMin, TimeUnit.MINUTES)  // Set the expiry time
                .maximumSize(cacheMaxSize);  // Set maximum cache size
    }
}
