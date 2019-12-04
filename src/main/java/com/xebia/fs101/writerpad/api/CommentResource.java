package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.request.CommentRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.CommentService;
import com.xebia.fs101.writerpad.service.SpamChecker;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/articles/")
public class CommentResource {
    @Autowired
    private CommentService commentService;
    @Autowired
    private SpamChecker spamChecker;
    @Autowired
    private ArticleService articleService;

    @PostMapping(path = "{slug_id}/comments")
    public ResponseEntity<Void> add(@PathVariable(value = "slug_id") String slugId,
                                    @Valid @RequestBody CommentRequest commentRequest,
                                    HttpServletRequest request) {

        try {
            if (spamChecker.isSpam(commentRequest.getBody())) {
                return ResponseEntity.status(BAD_REQUEST).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<Article> article = articleService.findOne(slugId);
        if (article.isPresent()) {
            Comment comment = commentRequest.toComment(article.get(),
                                                       request.getRemoteAddr());
            commentService.postComment(comment);
            return ResponseEntity.status(CREATED).build();
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @GetMapping(path = "{slug_id}/comments")
    public ResponseEntity<List<Comment>> get(@PathVariable(value = "slug_id") String slugId) {

        if (articleService.findOne(slugId).isPresent()) {
            List<Comment> comments = commentService.getAll(
                    StringUtils.extractUuid(slugId));
            return new ResponseEntity<>(comments, OK);
        }
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @DeleteMapping(path = "/{slug_id}/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "slug_id") String slugId,
                                       @PathVariable(value = "id") Long id) {

        if (articleService.findOne(slugId).isPresent()) {
            return this.commentService.deleteComment(id)
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}
