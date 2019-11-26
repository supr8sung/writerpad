package com.xebia.fs101.writerpad.api;


import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleRequest articleRequest) {

        try {
            Article savedArticle = articleService.addArticle(articleRequest);
            System.out.println("Data saved successfully");
            return new ResponseEntity<Article>(savedArticle, HttpStatus.CREATED);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
