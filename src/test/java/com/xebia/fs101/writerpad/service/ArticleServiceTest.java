package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlagiarismFinderService plagiarismFinderService;
    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setup() {

        articleService.averageTime = 225;
    }

    @Test
    void should_be_able_to_save_an_article() {
        when(plagiarismFinderService.isPlagiarism(any(),any())).thenReturn(false);
        articleService.add(new Article(), new User());
        verify(articleRepository).save(any());
    }

    @Test
    void should_be_able_to_delete_an_article() {

        UUID id = UUID.randomUUID();
        User user = new User.Builder().withPassword("").withEmail("").withUserName(
                "").build();
        Article article = new Article.Builder().withBody("").withTitle(
                "").withDescription("").build();
        article.setUser(user);
        when(articleRepository.findById(id)).thenReturn(Optional.of(article));
        when(userRepository.getOne(any())).thenReturn(user);
        articleService.delete("slug" + id, user);
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

        Article article = new Article.Builder().withBody("body").withDescription(
                "desc").withTitle("title").build();
        User user = new User.Builder().withUserName("ss").withEmail(
                "abc@gmail.com").withPassword("1234").build();
        article.setUser(user);
        when(articleRepository.findById(any())).thenReturn(
                Optional.of(article));
        when(userRepository.getOne(any())).thenReturn(user);
        when(plagiarismFinderService.isPlagiarism(any(),any())).thenReturn(false);

        articleService.update("id" + UUID.randomUUID(), article, user);
        verify(articleRepository).findById(any());
        verify(articleRepository).save(any());
        verify(articleRepository).findAll();
        verifyNoMoreInteractions(articleRepository);
    }

//    @Test
//    public void should_calulcate_reading_time_for_any_article() {
//
//        String body = IntStream.range(1, 226).mapToObj(String::valueOf).collect(
//                Collectors.joining(" "));
//        Article article = new Article.Builder().withDescription("description").withTitle(
//                "title").withBody(body).build();
//        ArticleService articleService = new ArticleService();
//        articleService.averageTime = 225;
//        ReadingTimeResponse readingTimeResponse = articleService.calculateReadingTime(
//                article);
//        assertThat(readingTimeResponse).isNotNull();
//        assertThat(readingTimeResponse.getReadingTime().getSeconds()).isEqualTo(0);
//        assertThat(readingTimeResponse.getReadingTime().getMins()).isEqualTo(1);
//    }
}