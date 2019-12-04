package com.xebia.fs101.writerpad.service;

import java.util.concurrent.ExecutionException;

public interface EmailService {

    void sendMail() throws ExecutionException, InterruptedException;

}
