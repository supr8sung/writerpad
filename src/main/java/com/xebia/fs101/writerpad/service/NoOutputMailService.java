package com.xebia.fs101.writerpad.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("test")
public class NoOutputMailService implements EmailService {
    @Override
    public void sendMail() {

        System.out.println("Mail sent successfully");
    }

}
