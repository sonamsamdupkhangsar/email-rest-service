package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(EmailHandler.class);
    @Autowired
    private EmailService emailService;

    /**
     * Send email
     * if exception occurs throws a bad request with exception message
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> email(ServerRequest serverRequest) {
        LOG.info("send email");
        return serverRequest.bodyToMono(Email.class)
                .doOnNext(email -> {LOG.info("email validate"); email.validate();})
                .doOnNext(email -> {
                    LOG.info("send email with service");
                emailService.sendEmail(email.getFrom(), email.getTo(),
                        email.getSubject(), email.getBody());})
                .flatMap(body -> {
                    LOG.info("creating uri for post");
                 return   ServerResponse.ok()/*created(URI.create("/email"))*/.contentType(MediaType.APPLICATION_JSON).build();
                })
                .onErrorResume(e -> {
                    LOG.info("error occured: {}", e);
                    return ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage()));
                });
    }
}
