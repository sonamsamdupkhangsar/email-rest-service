package me.sonam.email;

import lombok.extern.java.Log;
import me.sonam.MockConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@AutoConfigureWebTestClient
@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MockConfig.class, EmailService.class})
public class EmailServiceIntegTest {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceIntegTest.class);

    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmail() {
        LOG.info("sending email to service");
        emailService.sendEmail("from@1234domain.com", "to@sonam.me", "subject", "body");
    }


    @Test
    public void sendNullEmail() {
        LOG.info("sending email to service");
        try {
            emailService.sendEmail(null, null, null, null);
        }
        catch (Exception e) {
            LOG.info("exception expected on null object");
        }
    }
}
