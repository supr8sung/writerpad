package com.xebia.fs101.writerpad.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {
    @Test
    void should_create_slug_for_a_given_title() {
        String input = " this is title ";
        String output = StringUtils.slugify(input);
        assertThat(output).isEqualTo("this-is-title");
    }

    @Test
    public void should_extract_uuid() {
        UUID slugUuid = StringUtils.extractUuid("how-to-learn-spring-boot-03bc41f1-2f62-4aba-999e-456d0975300c");
        assertThat(slugUuid).toString().equals("03bc41f1-2f62-4aba-999e-456d0975300c");
    }

}