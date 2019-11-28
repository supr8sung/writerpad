package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;


    public Article add(ArticleRequest articleRequest) {

        Article article = new Article.Builder().withTitle(articleRequest.getTitle())
                .withBody(articleRequest.getBody())
                .withDescription(articleRequest.getDescription())
                .withTags(articleRequest.getTags().stream()
                        .map(tag -> tag.replaceAll(" ", "-").toLowerCase())
                        .collect(Collectors.toList()))
                .build();
        Article savedArticle = articleRepository.save(article);
        return savedArticle;
    }


    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }


    public Optional<Article> findOne(String slugId) {
        UUID id = StringUtils.extractUuid(slugId);

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent())
            return Optional.empty();
        return optionalArticle;
    }

    public boolean delete(String slugId) {
        UUID id = StringUtils.extractUuid(slugId);
        if (articleRepository.findById(id).isPresent()) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;

    }


    public Optional<Article> update(String slugId, Article copyFrom) {
        UUID id = StringUtils.extractUuid(slugId);

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        Article articleToBeUpdated = article.update(copyFrom);
        return Optional.of(articleRepository.save(articleToBeUpdated));


    }


}
