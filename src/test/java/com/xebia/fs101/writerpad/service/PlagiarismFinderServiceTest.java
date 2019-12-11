package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PlagiarismFinderServiceTest {
    PlagiarismFinderService plagiarismFinderService=new PlagiarismFinderService(0.70);
    @Test
    void should_return_true_if_string_is_same(){


        boolean bodyRedundant = plagiarismFinderService.isPlagiarism("body",
                                                                     Arrays.asList("avcf", "body",
                                                                          "bfgte"));
        assertThat(bodyRedundant).isTrue();
    }

    @Test
    void should_return_false_if_strings_is_not_similar() {

        boolean bodyRedundant = plagiarismFinderService.isPlagiarism("abcd",
                                                                     Arrays.asList("iemfeif",
                                                                          "fmefe",
                                                                          "ejfnenfe"));
        assertThat(bodyRedundant).isFalse();
    }
}