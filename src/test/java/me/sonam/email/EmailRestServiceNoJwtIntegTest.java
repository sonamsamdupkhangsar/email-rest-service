package me.sonam.email;

import lombok.extern.java.Log;
import me.sonam.Application;
import me.sonam.MockConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * test the email rest route
 */
@AutoConfigureWebTestClient
@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MockConfig.class, Application.class})
public class EmailRestServiceNoJwtIntegTest {
  private static final Logger LOG = LoggerFactory.getLogger(EmailRestServiceNoJwtIntegTest.class);

  @Autowired
  private WebTestClient client;

  @Test
  public void sendEmail() {
    LOG.info("sending email");
    final String authId = "user3";


    EntityExchangeResult<String> entityExchangeResult = client.post().uri("/emails").
            body(BodyInserters.fromValue(new Email("from@sonam.cloud", "to@sonam.cloud",
                    "welcome", "This is a welcome message.")))
            .exchange().expectStatus().isUnauthorized().expectBody(String.class).returnResult();
    LOG.info("result: {}, httpStatus: {}", entityExchangeResult.getResponseBody(), entityExchangeResult.getStatus());
  }


}
