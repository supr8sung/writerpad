package com.xebia.fs101.writerpad.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

//    @Test
//    void article_repository_is_not_null() {
//
//        Assertions.assertThat(articleRepository).isNotNull();
//
//    }

    @Test
    void should_find_all_tags() {


    }
}