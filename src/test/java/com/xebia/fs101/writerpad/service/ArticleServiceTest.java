package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ReadingTime;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.response.ReadingTimeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleService articleService;


    @BeforeEach
    void setup() {

        articleService.averageTime=225;
    }

    @Test
    void should_be_able_to_save_an_article() {

        ArticleRequest articleRequest =
                new ArticleRequest.Builder().withBody("body").withDescription("description").withTitle("title").build();
        articleService.add(articleRequest);
        verify(articleRepository).save(any());
    }

    @Test
    void should_be_able_to_delete_an_article() {

        UUID id = UUID.randomUUID();
        when(articleRepository.findById(id)).thenReturn(Optional.of(new Article()));
        articleService.delete("slug" + id);
        verify(articleRepository).findById(id);
        verify(articleRepository).deleteById(id);
    }

    @Test
    public void should_be_able_to_find_an_article() {

        UUID id = UUID.randomUUID();
        when(articleRepository.findById(any())).thenReturn(Optional.of(new Article()));
        articleService.findOne("slug" + id);
        verify(articleRepository).findById(id);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_give_all_the_articles() {

        Pageable pageable = null;
        articleService.findAll(pageable);
        verify(articleRepository).findAll(pageable);
    }

    @Test
    public void should_be_able_to_update_an_article() {

        Article article = new Article.Builder().withBody("abc").withDescription("efef").withTitle("fefe").build();
        when(articleRepository.findById(any())).thenReturn(Optional.ofNullable(new Article()));
        when(articleRepository.save(any())).thenReturn(new Article());
        articleService.update("id" + UUID.randomUUID(), article);
        verify(articleRepository).findById(any());
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    public void should_calulcate_reading_time_for_any_article() {

        Article article = new Article.Builder().withDescription("description").withTitle("title").withBody(".Lefteris " +
                "is a Lead Software Engineer at ZuluTrade and has been responsible for re-architecting the backend of" +
                " the main website from a monolith to event-driven microservices using Java, Spring Boot/Cloud, " +
                "RabbitMQ, Redis. He has extensive work experience for over 10 years in Software Development, working" +
                " mainly in the FinTech and Sports Betting industries. Prior to joining ZuluTrade, Lefteris worked as" +
                " a Senior Java Developer at Inspired Gaming Group in London, building enterprise sports betting " +
                "applications for William Hills and Paddy Power. He enjoys working with large-scalable, real-time and" +
                " high-volume systems deployed into AWS and wants to combine his passion for technology and traveling" +
                " by attending software conferences all over the world.").build();

        ArticleService articleService = new ArticleService();
        articleService.averageTime=225;
        ReadingTimeResponse readingTimeResponse = articleService.calculateReadingTime(article);
        assertThat(readingTimeResponse).isNotNull();
        assertThat(readingTimeResponse.getReadingTime().getSeconds()).isEqualTo(29);

    }

    @Test
    void should_get_all_tags_from_table() {
        articleService.getAllTags2();
 //       verify(articleRepository).findAllTags();
        verifyNoMoreInteractions(articleRepository);

    }
}