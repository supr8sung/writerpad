package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.exception.ArticleNotFoundException;
import com.xebia.fs101.writerpad.representations.ArticleResponse;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.response.ReadingTimeResponse;
import com.xebia.fs101.writerpad.response.TagsResponse;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.CommentService;
import com.xebia.fs101.writerpad.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
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
    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<Article>> getAll(Pageable pageable) {

        Page<Article> pageResult = articleService.findAll(pageable);
        if (!pageResult.hasContent())
            ResponseEntity.status(NO_CONTENT).build();
        return new ResponseEntity<>(pageResult.getContent(), OK);
    }

    @RequestMapping(params = "status", method = GET)
    public ResponseEntity<List<ArticleResponse>> getAllByStatus(@RequestParam(value =
            "status"
    ) String status, Pageable pageable) {

        Page<Article> pageResult = articleService.findAllByStatus(status, pageable);
        if (!pageResult.hasContent())
            return ResponseEntity.status(NO_CONTENT).build();
        List<ArticleResponse> articleResponses = pageResult.getContent().stream().map(
                ArticleResponse::from).collect(Collectors.toList());
        return new ResponseEntity<>(articleResponses, OK);
    }

    @PostMapping
    public ResponseEntity<ArticleResponse>
    create(@AuthenticationPrincipal User user,
           @Valid @RequestBody ArticleRequest articleRequest) {

        Article article = new Article.Builder().
                withTitle(articleRequest.getTitle()).withBody(
                articleRequest.getBody()).withDescription(
                articleRequest.getDescription()).withTags(articleRequest.getTags().
                stream().map(tag -> tag.replaceAll(" ", "-").toLowerCase()).collect(
                Collectors.toList())).build();
        Article savedArticle = articleService.add(article, user);
        return new ResponseEntity<>(ArticleResponse.from(savedArticle), CREATED);
    }

    @PatchMapping(path = "/{slug_id}")
    public ResponseEntity<ArticleResponse> update(@AuthenticationPrincipal User user,
                                                  @RequestBody ArticleRequest articleRequest,
                                                  @PathVariable(value = "slug_id") String slugId) {

        Article article = articleRequest.toArticle();
        Article updatedArticle = articleService.update(slugId, article, user);
        return new ResponseEntity<>(ArticleResponse.from(updatedArticle), OK);
    }

    @GetMapping(path = "/{slug_id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable(value = "slug_id") String slugId) {

        Article article = articleService.findOne(slugId);
        return new ResponseEntity<>(ArticleResponse.from(article), OK);
    }

    @DeleteMapping(path = "/{slug_id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user,
                                       @PathVariable(value = "slug_id") String slugId) {

        articleService.delete(slugId, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{slug_id}/{status}")
    public ResponseEntity<Void> articlePublish(
            @PathVariable(value = "slug_id") String slugId, @PathVariable(value =
            "status") String status) throws ExecutionException, InterruptedException {

        Article article = articleService.findOne(slugId);
        Optional<Article> publishedArticle = articleService.publish(article);
        if (publishedArticle.isPresent()) {
            emailService.sendMail();
            return ResponseEntity.status(NO_CONTENT).build();
        } else
            return ResponseEntity.status(BAD_REQUEST).build();
    }

    @GetMapping(path = "{slug_id}/timetoread")
    public ResponseEntity<ReadingTimeResponse> readingTime(@PathVariable(value =
            "slug_id") String slugId) {

        Article article = articleService.findOne(slugId);
        ReadingTimeResponse readingTimeResponse = articleService.calculateReadingTime(
                article);
        return new ResponseEntity<>(readingTimeResponse, OK);
    }

    @GetMapping(path = "/tags")
    public ResponseEntity<List<TagsResponse>> tags() {

        Map<String, Long> mappedTags = articleService.getAllTags();
        if (mappedTags.isEmpty())
            return ResponseEntity.notFound().build();
        List<TagsResponse> collectedTags = mappedTags.entrySet().stream()
                .map(e -> new TagsResponse(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(collectedTags, OK);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    @PutMapping("{slug_id}/favourite")
    public ResponseEntity<Void> markFavorite(@PathVariable(value = "slug_id") String slugId) {

        articleService.markFavourite(slugId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("{slug_id}/unfavourite")
    public ResponseEntity<Void> deleteFavourite(@PathVariable(value = "slug_id") String slugId) {

        articleService.deleteFavourite(slugId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
