package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Article>> getAll(Pageable pageable) {

        Page<Article> pageResult = articleService.findAll(pageable);
        if (!pageResult.hasContent()) {
            ResponseEntity.noContent().build();
        }
        List<Article> articles = pageResult.getContent();
        return new ResponseEntity<>(articles, OK);
    }

    @RequestMapping(path = "/{status}", params = "status", method = GET)
    public ResponseEntity<List<Article>> getAllByStatus(
            @RequestParam(value = "status", required = false) String status, Pageable pageable) {

        Page<Article> pageResult =
                articleService.findAllByStatus(
                        ArticleStatus.valueOf(status.toUpperCase()), pageable);
        if (!pageResult.hasContent())
            return ResponseEntity.status(NO_CONTENT).build();
        List<Article> articles = pageResult.getContent();
        return new ResponseEntity<>(articles, OK);
    }

    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleRequest articleRequest) {

        Article savedArticle = articleService.add(articleRequest);
        System.out.println("Data saved successfully");
        return new ResponseEntity<Article>(savedArticle, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{slug_id}")
    public ResponseEntity<Article> getById(@PathVariable(value = "slug_id") String slugId) {

        Optional<Article> article = articleService.findOne(slugId);
        if (!article.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<Article>(article.get(), OK);
    }

    @DeleteMapping(path = "/{slug_id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "slug_id") String slugId) {

        boolean deleted = articleService.delete(slugId);
        if (deleted)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping(path = "/{slug_id}")
    public ResponseEntity<Article> update(
            @RequestBody ArticleRequest articleRequest,
            @PathVariable(value = "slug_id") String slugId) {

        Article article = articleRequest.toArticle();
        Optional<Article> updatedArticle = articleService.update(slugId, article);
        return updatedArticle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/{slug_id}/{status}")
    public ResponseEntity<Void> publish(
            @PathVariable(value = "slug_id") String slugId
            , @PathVariable(value = "status") String status) {

        Optional<Article> article = articleService.findOne(slugId);
        if (article.isPresent())
            return articleService.publish(article.get());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }

}

