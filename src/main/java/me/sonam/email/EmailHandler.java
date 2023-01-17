package me.sonam.email;

import com.sun.tools.javac.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
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

    @Value("${EMAIL_HOST}")
    private String emailHost;

    @Value("${EMAIL_PORT}")
    private int emailPort;

    @Value("${EMAIL_USERNAME}")
    private String emailUserName;

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

    @PostConstruct
    public void log() {
        LOG.info("emailHost: {}, emailPort: {}, emailUserName: {}, emailPassword: {}",
                emailHost, emailPort, emailUserName, emailPassword);
    }
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
                                 bodyValue(getMap(Pair.of("message", "email successfully sent")));
                })
                .onErrorResume(e -> {
                    LOG.info("failed to send email", e);
                    return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(getMap(Pair.of("error", "failed to send email")));
                });
    }

    private Map<String, String> getMap(Pair<String, String>... pairs){

        Map<String, String> map = new HashMap<>();

        for(Pair<String, String> pair: pairs) {
            map.put(pair.fst, pair.snd);
        }
        return map;

    }
}
