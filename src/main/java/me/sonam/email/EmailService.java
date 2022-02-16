package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Business service that sends email using autoconfigured {@link JavaMailSender}
 */
@Service
public class EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${EMAIL_HOST}")
    private String emailHost;

    @Value("${EMAIL_PORT}")
    private String emailPort;

    @Value("${EMAIL_USERNAME}")
    private String emailUserName;

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

    public void sendEmail(String from, String to, String subject, String body) {
        LOG.info("email host: {}, port: {}, username: {}, password: {}", emailHost, emailPort, emailUserName, emailPassword);
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(to);
        msg.setFrom(from);

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
        LOG.info("email sent");
    }
}
