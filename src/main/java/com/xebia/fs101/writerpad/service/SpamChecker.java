package com.xebia.fs101.writerpad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SpamChecker {
    @Value("${classpath:spamwords.txt}")
    private File file;
    Set<String> spamWords;
    private List<String> lines;

    @PostConstruct
    public void init() throws IOException {

        lines = Files.readAllLines(file.toPath());
        this.spamWords = new HashSet<>(lines);
    }

    public boolean isSpam(String content) throws IOException {

        Set<String> words = new HashSet<>(
                Arrays.asList(content.toLowerCase().split("\\s")));
        if (!Collections.disjoint(spamWords, words))
            return true;
        return false;
    }
}
