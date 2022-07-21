package me.sonam.email;

import me.sonam.MockConfig;
import lombok.extern.java.Log;
import me.sonam.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

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

  @Test
  public void sendEmail() {
    LOG.info("sending email");
    client.post().uri("/email").
            body(BodyInserters.fromValue(new Email("from@sonam.cloud", "to@sonam.cloud",
                    "welcome", "This is a welcome message.")))
            .exchange().expectStatus().isCreated();
  }

  @Test
  public void invalidEmailAddress() {
    LOG.info("sending email");
    client.post().uri("/email").
            body(BodyInserters.fromValue(new Email("fromsonam.cloud", "to@sonam.cloud",
                    "welcome", "This is a welcome message.")))
            .exchange().expectStatus().isBadRequest();

    client.post().uri("/email").
            body(BodyInserters.fromValue(new Email("from@sonam.cloud", "tosonam.cloud",
                    "welcome", "This is a welcome message.")))
            .exchange().expectStatus().isBadRequest();

    client.post().uri("/email").
            body(BodyInserters.fromValue(new Email("fromsonam.cloud", "tosonam.cloud",
                    "welcome", "This is a welcome message.")))
            .exchange().expectStatus().isBadRequest();

  }
}
