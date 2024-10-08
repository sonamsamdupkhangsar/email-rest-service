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
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Test the liveness and readiness endpoints
 */
@AutoConfigureWebTestClient
@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MockConfig.class, Application.class})
public class LivenessReadinesslRestServiceIntegTest {
  private static final Logger LOG = LoggerFactory.getLogger(LivenessReadinesslRestServiceIntegTest.class);

  @Autowired
  private WebTestClient client;

  @Test
  public void readiness() {
    LOG.info("check readiness endpoint");
    client.get().uri("/emails/api/health/readiness")
            .exchange().expectStatus().isOk();
  }

  @Test
  public void liveness() {
    LOG.info("check liveness endpoint");
    client.get().uri("/emails/api/health/liveness")
            .exchange().expectStatus().isOk();
  }

  @Test
  public void emails() {
    LOG.info("should fail when accessing emails path");
    client.post().uri("/emails").exchange().expectStatus().isUnauthorized();
  }
}
