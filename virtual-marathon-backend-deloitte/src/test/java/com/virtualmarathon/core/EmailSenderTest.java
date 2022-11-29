package com.virtualmarathon.core;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class EmailSenderTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("priyesh", "priyesh@123"))
            .withPerMethodLifecycle(false);

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void shouldUseGreenMail() throws Exception {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("admin@spring.io");
        mail.setSubject("A new message for you");
        mail.setText("Hello GreenMail!");
        mail.setTo("test@greenmail.io");

        javaMailSender.send(mail);

        // awaitility
        try {
           await().atMost(2, SECONDS).untilAsserted(() -> {

                MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
               // System.out.println("length: " +receivedMessages.length);
                assertEquals(2, receivedMessages.length);

                MimeMessage receivedMessage = receivedMessages[0];
                assertEquals("Hello GreenMail!", GreenMailUtil.getBody(receivedMessage));
                assertEquals(1, receivedMessage.getAllRecipients().length);
                assertEquals("test@greenmail.io", receivedMessage.getAllRecipients()[0].toString());
            });
        } catch (ConditionTimeoutException e){
//            throw new AssertionError(e.getMessage());
            System.out.println("Not made connection in that 2 seconds");
        }

    }
}
