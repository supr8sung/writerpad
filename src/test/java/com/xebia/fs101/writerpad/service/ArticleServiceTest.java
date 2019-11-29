package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

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

    @Test
    void should_be_able_to_post_an_article() {

        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
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

        UUID id=UUID.randomUUID();
        articleService.findOne("slug"+id);
        verify(articleRepository).findById(id);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_give_all_the_articles() {
        Pageable pageable = null;
        articleService.findAll(pageable);
        verify(articleRepository).findAll(pageable);

    }

//    @Test
////    public void should_be_able_to_update_an_article(){
////        Article article = new Article.Builder()
////                .withBody("abc")
////                .withDescription("efef")
////                .withTitle("fefe")
////                .build();
////
////        ArticleRequest updateArticle = new ArticleRequest.Builder()
////                .withBody(" body")
////                .withTitle("title")
////                .withDescription("description")
////                .build();
////        when(articleRepository.findById(any())).thenReturn(Optional.of(new Article()));
////        articleService.update("id"+UUID.randomUUID(),any());
////        verify(articleRepository).findById(any());
////        verify(articleRepository).save(any());
////        verifyNoMoreInteractions(articleRepository);
////
////    }



}