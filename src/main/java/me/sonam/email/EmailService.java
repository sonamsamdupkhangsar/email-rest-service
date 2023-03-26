package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Business service that sends email using autoconfigured {@link JavaMailSender}
 */
@Service
public class EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUser;

    public void sendEmail(String from, String to, String subject, String body) {
        LOG.info("send email with javaMailSender: from: {},\n to: {},\n subject: {}, body: {}",
        from, to, subject, body);
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(to);
        msg.setFrom(from);

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
        LOG.info("email sent");
    }

    @PostConstruct
    public void sendEmailOnConstruct() {
        LOG.info("send email on post construct");
        try {
            sendEmail(emailUser, "test1@sonam.email", "hello, testing", "hello testing body");
        }
        catch (Exception e) {
            LOG.error("failed to send email: {}", e.getMessage());
            throw e;
        }
    }
}
