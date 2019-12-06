package com.xebia.fs101.writerpad.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadingTimeTest {
    @Test
    void should_calculate_total_number_of_words_in_any_string() {

        String content = IntStream.range(1, 64).mapToObj(String::valueOf).collect(
                Collectors.joining(" "));
        ReadingTime calculateTime = ReadingTime.calculate(content, 60);
        assertThat(calculateTime.getSeconds()).isEqualTo(3);
        assertThat(calculateTime.getMins()).isEqualTo(1);

    }


}