package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

//    @Test
//    void should_find_all_tags() {
//
//        Article article = new Article.Builder()
//                .withTitle("title")
//                .withDescription("desc")
//                .withBody("body")
//                .withTags(Arrays.asList("t1", "t2"))
//                .build();
//        articleRepository.save(article);
//        Stream<String> tags = articleRepository.findTags();
//        Map<String, Long> collect = tags.collect(
//                Collectors.groupingBy(e -> e, Collectors.counting()));
//        Assertions.assertThat(collect.get("t1")).isEqualTo(1);
//
//    }
}