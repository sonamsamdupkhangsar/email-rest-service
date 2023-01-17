package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
                .flatMap(email -> {
                    LOG.info("returng success message");
                 return   ServerResponse.created(URI.create("/emails"))
                         .contentType(MediaType.APPLICATION_JSON).
                                 bodyValue(getMap(new Pair("message", "email successfully sent")));
                })
                .onErrorResume(e -> {
                    LOG.info("failed to send email", e);
                    return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(getMap(new Pair("error", "failed to send email")));
                });
    }

    private Map<String, String> getMap(Pair... pairs){

        Map<String, String> map = new HashMap<>();

        for(Pair pair: pairs) {
            map.put(pair.key, pair.value);
        }
        return map;

    }

    class Pair {
        public String key;
        public String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
