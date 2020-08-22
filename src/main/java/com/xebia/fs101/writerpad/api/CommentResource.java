package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.request.CommentRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.CommentService;
import com.xebia.fs101.writerpad.service.SpamChecker;
import com.xebia.fs101.writerpad.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
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
    public ResponseEntity create(@AuthenticationPrincipal User user, @PathVariable(value =
            "slug_id") String slugId,
                                 @Valid @RequestBody CommentRequest commentRequest,
                                 HttpServletRequest request) throws IOException {

        if (spamChecker.isSpam(commentRequest.getBody()))
            return ResponseEntity.status(BAD_REQUEST).build();
        commentService.postComment(commentRequest, slugId, request.getRemoteAddr());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping(path = "{slug_id}/comments")
    public ResponseEntity<List<Comment>> get(@AuthenticationPrincipal User user,
                                             @PathVariable(value = "slug_id") String slugId) {

        List<Comment> comments = commentService.getAll(
                StringUtils.extractUuid(slugId));
        return new ResponseEntity<>(comments, OK);
    }

    @DeleteMapping(path = "/{slug_id}/comments/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user,
                                       @PathVariable(value = "slug_id") String slugId,
                                       @PathVariable(value = "id") Long id) {

        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
