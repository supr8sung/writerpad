package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;


    public Article addArticle(ArticleRequest articleRequest) {

        Article article = new Article.Builder().withTitle(articleRequest.getTitle())
                .withBody(articleRequest.getBody())
                .withDescription(articleRequest.getDescription())
                .withSlug(articleRequest.getTitle().replaceAll(" " , "-").toLowerCase())
                .withSlug(articleRequest.getTitle()
                        .replaceAll(" ", "-")
                        .toLowerCase())
                .withTagList(articleRequest.getTags().stream()
                        .map(tag -> tag.replaceAll(" " , "-").toLowerCase())
                        .collect(Collectors.toList()))
                .build();
        Article savedArticle = articleRepository.save(article);
        return savedArticle;
    }

    public boolean isValid(ArticleRequest articleRequest) {
        if (articleRequest.getBody().isEmpty()
                || articleRequest.getTitle().isEmpty()
                || articleRequest.getDescription().isEmpty()) {
            return true;
        }
        return false;
    }
}
