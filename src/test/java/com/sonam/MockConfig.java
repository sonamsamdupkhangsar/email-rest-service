package com.sonam;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MockConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MockConfig.class);

    @Bean("testJavaMailSender")
    public JavaMailSender javaMailSender() {
        LOG.info("mocking javaMailSender bean");
        return Mockito.mock(JavaMailSender.class);
    }

}
