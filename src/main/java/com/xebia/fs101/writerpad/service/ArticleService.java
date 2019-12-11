package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.exception.ArticleNotFoundException;
import com.xebia.fs101.writerpad.exception.UserNotAuthorizedException;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.model.ReadingTime;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.response.ReadingTimeResponse;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.xebia.fs101.writerpad.model.ArticleStatus.PUBLISHED;

@Service
public class ArticleService {
    @Value("${average.words.per.minute}")
    int averageTime;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    public Article add(Article article, User user) {

        User foundUser = userRepository.getOne(user.getId());
        article.setUser(foundUser);
        return this.articleRepository.save(article);
    }

    public Page<Article> findAll(Pageable pageable) {

        return articleRepository.findAll(pageable);
    }

    public Page<Article> findAllByStatus(String status, Pageable pageable) {

        return articleRepository.findAllByStatus(
                ArticleStatus.valueOf(status.toUpperCase()), pageable);
    }

    public Article findOne(String slugId) {

        return articleRepository.findById(StringUtils.extractUuid(slugId)).orElseThrow(
                ArticleNotFoundException::new);
    }

    public void delete(String slugId, User user) {

        UUID id = StringUtils.extractUuid(slugId);
        Article article = articleRepository.findById(id).orElseThrow(
                ArticleNotFoundException::new);
        if (!article.getUser().equals(userRepository.getOne(user.getId())))
            throw new UserNotAuthorizedException(
                    "User not athorized to delte the article");
        articleRepository.deleteById(id);
    }

    public Article update(String slugId, Article copyFrom, User user) {

        Article article = articleRepository.findById(StringUtils.extractUuid(slugId))
                .orElseThrow(ArticleNotFoundException::new);
        if (!article.getUser().equals(userRepository.getOne(user.getId())))
            throw new UserNotAuthorizedException(
                    "User not authorized to update the article");
        Article articleToBeUpdated = article.update(copyFrom);
        return articleRepository.save(articleToBeUpdated);
    }

    public ReadingTimeResponse calculateReadingTime(Article article) {

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

    @Transactional
    public Map<String, Long> getAllTags() {

        Stream<String> tags = articleRepository.findTags();
        return tags.collect(
                Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    public Article markFavourite(String slugId) {

        Article article = articleRepository.findById(StringUtils.extractUuid(slugId))
                .orElseThrow(ArticleNotFoundException::new);
        article.setFavorited(true);
        article.setFavoritesCount(article.getFavoritesCount() + 1);
        return articleRepository.save(article);
    }

    public Article deleteFavourite(String slugId) {

        Article article = articleRepository.findById(StringUtils.extractUuid(slugId))
                .orElseThrow(ArticleNotFoundException::new);
        if (article.getFavoritesCount() <= 1) {
            article.setFavorited(false);
            article.setFavoritesCount(0);
            return articleRepository.save(article);
        }
        article.setFavoritesCount(article.getFavoritesCount() - 1);
        return articleRepository.save(article);
    }
}
