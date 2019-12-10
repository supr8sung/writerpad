package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.response.ReadingTimeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setup() {

        articleService.averageTime = 225;
    }

    @Test
    void should_be_able_to_save_an_article() {

        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        articleService.add(new Article(),new User());
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

        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        when(articleRepository.findById(any())).thenReturn(
                Optional.ofNullable(new Article()));
        when(articleRepository.save(any())).thenReturn(new Article());
        articleService.update("id" + UUID.randomUUID(), article);
        verify(articleRepository).findById(any());
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    public void should_calulcate_reading_time_for_any_article() {

        String body = IntStream.range(1, 226).mapToObj(String::valueOf).collect(Collectors.joining(" "));
        System.out.println(body);
        Article article = new Article.Builder().withDescription("description").withTitle(
                "title").withBody(body).build();
        ArticleService articleService = new ArticleService();
        articleService.averageTime = 225;
        ReadingTimeResponse readingTimeResponse = articleService.calculateReadingTime(
                article);
        assertThat(readingTimeResponse).isNotNull();
        assertThat(readingTimeResponse.getReadingTime().getSeconds()).isEqualTo(0);
        assertThat(readingTimeResponse.getReadingTime().getMins()).isEqualTo(1);
    }
}