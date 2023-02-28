package me.sonam.email;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

public class EmailWithRemoteEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(EmailWithRemoteEndpoint.class);

    private WebTestClient webTestClient = WebTestClient.bindToServer().build();
    private  WebClient webClient = WebClient.builder().build();

    // port-forward email-rest-service to localhost:8082
    private final String emailEndpoint = "http://localhost:8083/email";

    /**
     * this test is for a live remote endpoint.
     */
    @Test
    public void signup() {
        LOG.info("email using port-forwarded endpoint");
        final String id = UUID.randomUUID().toString().replace("-", "");

       webTestClient.mutate().responseTimeout(Duration.ofSeconds(30)).build();

        webTestClient.post().uri(emailEndpoint).
                body(BodyInserters.fromValue(new Email("kecha@sonam.email", "me@sonam.email",
                        "welcome", "This is a welcome test message.")))
                .exchange().expectStatus().isOk().returnResult(String.class).consumeWith(stringFluxExchangeResult -> {
            LOG.info("result: {}", stringFluxExchangeResult.getResponseBody());
        });
    }

    @Test
    public void emailWebClient() {
        LOG.info("email user: {}", emailEndpoint);
        Email email = new Email("kecha@sonam.email", "me@sonam.email",
                "welcome", "This is a welcome test message.");


        /*WebClient.ResponseSpec spec = */
        webClient.post().uri(emailEndpoint)
                .body(BodyInserters.fromValue(email))
                .accept(MediaType.APPLICATION_JSON)
               .retrieve().bodyToMono(String.class).flatMap(s -> {
                    LOG.info("activation email response is: {}", s);
                    return Mono.just(s);
                });
    }


}
