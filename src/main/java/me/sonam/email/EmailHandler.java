package me.sonam.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * EmailHandler class to delegate business processing to {@link EmailService}
 */
@Component
public class EmailHandler {

    @Autowired
    private EmailService emailService;

    public Mono<ServerResponse> email(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Email.class)
                .doOnNext(body -> emailService.sendEmail(body))
                .flatMap(body -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build());
    }


}
