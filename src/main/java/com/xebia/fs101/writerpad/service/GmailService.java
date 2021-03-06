package com.xebia.fs101.writerpad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Profile("!test")
public class GmailService implements EmailService {
    private ExecutorService executorService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${email.receiver}")
    private String toEmail;
    @Value("${email.subject}")
    private String subject;
    @Value("${email.body}")
    private String body;
    @Value("$")
    private String property;

    public void sendMail() throws MailException {

        Runnable runnable = () -> {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailMessage.setFrom("supreetsingh106@gmail.com");
            javaMailSender.send(mailMessage);
        };
        this.executorService = Executors.newSingleThreadExecutor();
        executorService.submit(runnable);
    }
}
