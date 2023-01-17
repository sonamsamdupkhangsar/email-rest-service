package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Set Email route
 */
@Configuration
public class EmailRouter {
    private static final Logger LOG = LoggerFactory.getLogger(EmailRouter.class);

    @Bean
    public RouterFunction<ServerResponse> route(EmailHandler emailHandler) {
        LOG.info("building email router function");
        return RouterFunctions.route(POST("/emails").and(accept(MediaType.APPLICATION_JSON)),
                emailHandler::email);
    }
}
