package com.xebia.fs101.writerpad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

@Component
public class SpamChecker {
    private Set<String> spamWords;


    private List<String> lines;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws IOException {
        File file = resourceLoader.getResource("classpath:spamwords.txt").getFile();
        lines = Files.readAllLines(file.toPath());

    }


    public boolean isSpam(String content) throws IOException {


        String[] words = content.toLowerCase().split("\\s");
        for (String word : words) {
            if (lines.contains(word))
                return true;

        }
        return false;

    }


}
