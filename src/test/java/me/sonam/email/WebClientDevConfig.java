package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientDevConfig {
    private static final Logger LOG = LoggerFactory.getLogger(WebClientDevConfig.class);

    @Bean
    public WebClient.Builder webClientBuilder() {
        LOG.info("returning non-loadbalanced webclient");
        return WebClient.builder();
    }

}


