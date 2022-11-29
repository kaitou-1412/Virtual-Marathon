package com.virtualmarathon.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailSenderServices {

    @Autowired
    private JavaMailSender mailSender;

    public  void sendEmail(String toEmail, String body, String subject){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail sent");
    }

    public  void sendEmailWithInlineImage(String toEmail,
                                         String body,
                                         String subject,
                                          ClassPathResource res) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body , true);
        mimeMessageHelper.addInline("identifier1", res);

        mailSender.send(mimeMessage);
        System.out.println("Mail sent");
    }
}
