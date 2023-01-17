package me.sonam.email;

import lombok.extern.java.Log;
import me.sonam.Application;
import me.sonam.MockConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * test the email rest route
 */
@AutoConfigureWebTestClient
@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MockConfig.class, Application.class})
public class EmailRestServiceIntegTest {
  private static final Logger LOG = LoggerFactory.getLogger(EmailRestServiceIntegTest.class);

  @Autowired
  private WebTestClient client;

  @MockBean
  private ReactiveJwtDecoder reactiveJwtDecoder;

  @Before
  public void setUp() {
    LOG.info("setup mock");
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void sendEmail() {
    LOG.info("sending email");
    final String authId = "user3";
    Jwt jwt = jwt(authId);
    when(this.reactiveJwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));


    client.post().uri("/emails").
            body(BodyInserters.fromValue(new Email("from@sonam.cloud", "to@sonam.cloud",
                    "welcome", "This is a welcome message.")))
            .headers(addJwt(jwt))
            .exchange().expectStatus().isCreated().returnResult(String.class).consumeWith(stringFluxExchangeResult -> {
              stringFluxExchangeResult.getResponseBody().subscribe(s -> LOG.info("response: {}", s));
    });
  }

  @Test
  public void invalidEmailAddress() {
    LOG.info("sending email");
    final String authId = "user3";
    Jwt jwt = jwt(authId);
    when(this.reactiveJwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));


    client.post().uri("/emails").
            body(BodyInserters.fromValue(new Email("fromsonam.cloud", "to@sonam.cloud",
                    "welcome", "This is a welcome message.")))
            .headers(addJwt(jwt))
            .exchange().expectStatus().isBadRequest();

    client.post().uri("/emails").
            body(BodyInserters.fromValue(new Email("from@sonam.cloud", "tosonam.cloud",
                    "welcome", "This is a welcome message.")))
            .headers(addJwt(jwt))
            .exchange().expectStatus().isBadRequest();

    client.post().uri("/emails").
            body(BodyInserters.fromValue(new Email("fromsonam.cloud", "tosonam.cloud",
                    "welcome", "This is a welcome message.")))
            .headers(addJwt(jwt))
            .exchange().expectStatus().isBadRequest();

  }


  private Jwt jwt(String subjectName) {
    return new Jwt("token", null, null,
            Map.of("alg", "none"), Map.of("sub", subjectName));
  }

  private Consumer<HttpHeaders> addJwt(Jwt jwt) {
    return headers -> headers.setBearerAuth(jwt.getTokenValue());
  }
}
