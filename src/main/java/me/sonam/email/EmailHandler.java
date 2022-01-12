package me.sonam.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
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

    /**
     * Send email
     * if exception occurs throws a bad request with exception message
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> email(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Email.class)
                .doOnNext(email -> email.validate())
                .doOnNext(email -> emailService.sendEmail(email.getFrom(), email.getTo(),
                        email.getSubject(), email.getBody()))
                .flatMap(body -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build())
                .onErrorResume(e ->  ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }
}
