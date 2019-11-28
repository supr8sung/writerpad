package com.xebia.fs101.writerpad.service;


import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
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

    public Comment postComment(UUID id, Comment comment) {

        Comment savedComment = commentRepository.save(comment);
        return savedComment;
    }

    public List<Comment> getAll(UUID id) {
        return commentRepository.findByArticleId(id);
    }

    public boolean deleteComment(Long id) {
        if (commentRepository.findById(id).isPresent()) {
            System.out.println("hervvbruvrurvrvruu");
            System.out.println(commentRepository.findById(id).get());
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
