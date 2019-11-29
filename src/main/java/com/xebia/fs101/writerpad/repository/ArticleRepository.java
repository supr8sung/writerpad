package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
