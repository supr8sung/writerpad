package com.xebia.fs101.writerpad.service;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SpamChecker {
    //    @Value("classpath:spamwords.txt")
//    private File file;
    Set<String> spamWords;
    private List<String> lines = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
//        lines = Files.readAllLines(file.toPath());
//        this.spamWords = new HashSet<>(lines);
    }

    public boolean isSpam(String content) throws IOException {

        File file = ResourceUtils.getFile("classpath:spamwords.txt");
        FileInputStream input = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        spamWords = new HashSet<>(lines);
        Set<String> words = new HashSet<>(
                Arrays.asList(content.toLowerCase().split("\\s")));
        if (!Collections.disjoint(spamWords, words))
            return true;
        return false;
    }
}
