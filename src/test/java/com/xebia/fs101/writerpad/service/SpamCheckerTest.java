package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class SpamCheckerTest {



    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private SpamChecker spamChecker;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(resourceLoader.getResource(ArgumentMatchers.anyString())).thenReturn(new ClassPathResource("spamwords.txt"));
        spamChecker.init();
    }

    @Test
    void should_return_true_if_spam_found() throws IOException {

        boolean isSpam=false;
        try {
             isSpam = spamChecker.isSpam("adult");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(isSpam).isTrue();
    }

    @Test
    void should_return_false_if_no_spam_found() throws IOException {


        boolean isSpam=false;

            isSpam = spamChecker.isSpam("bhai bhai bhai");

        assertThat(isSpam).isFalse();
    }
}