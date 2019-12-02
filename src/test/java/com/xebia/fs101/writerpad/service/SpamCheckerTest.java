package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpamCheckerTest {
    private SpamChecker spamChecker;

    @BeforeEach
    void setup() {

        spamChecker = new SpamChecker();
        spamChecker.spamWords = new HashSet<>(Arrays.asList("adult"));
    }

    @Test
    void should_return_true_if_spam_found() throws IOException {

        boolean isSpam = spamChecker.isSpam("adult");
        assertThat(isSpam).isTrue();
    }

    @Test
    void should_return_false_if_no_spam_found() throws IOException {

        boolean isSpam = spamChecker.isSpam("bhai bhai bhai");
        assertThat(isSpam).isFalse();
    }

}