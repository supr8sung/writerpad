package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(UUID articleId);

}
