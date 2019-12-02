package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xebia.fs101.writerpad.model.ArticleStatus.PUBLISHED;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EmailService emailService;

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

    public Page<Article> findAllByStatus(ArticleStatus status, Pageable pageable) {

        return articleRepository.findAllByStatus(status, pageable);
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
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            Article articleToBeUpdated = article.update(copyFrom);
            return Optional.of(articleRepository.save(articleToBeUpdated));
        }
        return Optional.empty();
    }

    public Optional<Article> publish(Article article) {

        Optional<Article> publishedArticle = null;

        if (article.getStatus() == PUBLISHED)
            publishedArticle = Optional.empty();
        else {
            sendEmail();
            articleRepository.updateStatus(PUBLISHED, article.getId());
            publishedArticle = Optional.of(article);
        }
        return publishedArticle;
    }

    public void sendEmail() {

        try {
            emailService.sendMail("supreet.singh@xebia.com"
                    , "Congratulations"
                    , "Hello dost, your blog has been successfully published");
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

}
