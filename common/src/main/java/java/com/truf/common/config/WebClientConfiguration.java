package java.com.truf.common.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
public class WebClientConfiguration {

    public static final int FLUSH_TIMEOUT = 10;
    public static final int READ_TIMEOUT = 10;
    public static final int CONNECTION_TIMEOUT = 30;
    @Value("${webclient.maxInMemorySize}")
    private Integer maxInMemorySize;
    @Bean("webClient")
    public WebClient getWebClient() throws SSLException {

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(spec -> spec.sslContext(sslContext)
                        .handshakeTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT))
                        .closeNotifyFlushTimeout(Duration.ofSeconds(FLUSH_TIMEOUT))
                        .closeNotifyReadTimeout(Duration.ofSeconds(READ_TIMEOUT)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize * 1024 * 1024))
                .build();
    }
}
