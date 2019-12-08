package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.exception.CommnetNotFoundException;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.request.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;

    public Comment postComment(CommentRequest commentRequest, String slugId,
                               String ipAddress) {

        Article article = articleService.findOne(slugId);
        Comment comment = commentRequest.toComment(article,
                                                   ipAddress);
        return commentRepository.save(comment);
    }

    public List<Comment> getAll(UUID id) {

        return commentRepository.findByArticleId(id);
    }

    public boolean deleteComment(Long id) {

        commentRepository.findById(id).orElseThrow(
                CommnetNotFoundException::new);
        commentRepository.deleteById(id);
        return true;
    }
}
