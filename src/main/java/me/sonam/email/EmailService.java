package me.sonam.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void sendEmail(Email email) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(email.getTo());
        msg.setFrom(email.getFrom());

        msg.setSubject(email.getSubject());
        msg.setText(email.getBody());

        javaMailSender.send(msg);
        LOG.info("email sent");
    }

}
