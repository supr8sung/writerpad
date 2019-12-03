package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.model.ReadingTime;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.response.ReadingTimeResponse;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xebia.fs101.writerpad.model.ArticleStatus.PUBLISHED;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article add(ArticleRequest articleRequest) {

        Article article = new Article.Builder().
                withTitle(articleRequest.getTitle()).withBody(
                articleRequest.getBody()).withDescription(
                articleRequest.getDescription()).withTags(articleRequest.getTags().
                stream().map(tag -> tag.replaceAll(" ", "-").toLowerCase()).collect(
                Collectors.toList())).build();
        return articleRepository.save(article);
    }

    public Page<Article> findAll(Pageable pageable) {

        return articleRepository.findAll(pageable);
    }

    public Page<Article> findAllByStatus(ArticleStatus status, Pageable pageable) {

        return articleRepository.findAllByStatus(status, pageable);
    }

    public Optional<Article> findOne(String slugId) {

        return articleRepository.findById(StringUtils.extractUuid(slugId));
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

        Optional<Article> optionalArticle = articleRepository.findById(
                StringUtils.extractUuid(slugId));
        if (optionalArticle.isPresent()) {
            Article articleToBeUpdated = optionalArticle.get().update(copyFrom);
            return Optional.of(articleRepository.save(articleToBeUpdated));
        }
        return Optional.empty();
    }

    public ReadingTimeResponse calculateReadingTime(Article article, int averageTime) {

        ReadingTime readingTime = ReadingTime.calculate(article.getBody(), averageTime);
        String slugId = article.getSlug() + "_" + article.getId();
        return new ReadingTimeResponse(slugId, readingTime);
    }

    public Optional<Article> publish(Article article) {

        if (article.getStatus() == PUBLISHED)
            return Optional.empty();
        else {
            article.publish();
            return Optional.of(articleRepository.save(article));
        }
    }

}
