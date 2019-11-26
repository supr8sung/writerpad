package com.xebia.fs101.writerpad.api;


import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleRequest;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleRequest articleRequest;

    @PostMapping
    public Article create(@RequestBody ArticleRequest articleRequest,
                          HttpServletResponse response) {
        if (articleRequest.getBody() == null || articleRequest.getBody().equals("")
                || articleRequest.getTitle() == null
                || articleRequest.getTitle().equals("")
                || articleRequest.getDescription() == null
                || articleRequest.getDescription().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else
            response.setStatus(HttpStatus.CREATED.value());
        Article article = new Article.Builder().withTitle(articleRequest.getTitle())
                .withBody(articleRequest.getBody())
                .withDescription(articleRequest.getDescription())
                .withSlug(articleRequest.getTitle()
                .replaceAll(" ", "-").toLowerCase())
                .withTagList(articleRequest.getTags())
                .build();
        Article savedArticle = articleRepository.save(article);

        System.out.println(savedArticle.getId());
        System.out.println("Data saved successfully");
        return savedArticle;
    }

}
