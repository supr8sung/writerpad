package com.xebia.fs101.writerpad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WriterpadApplication {
    public static void main(String[] args) {

        SpringApplication.run(WriterpadApplication.class, args);
    }
}
