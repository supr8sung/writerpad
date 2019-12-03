package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

@Component
@Transactional
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Query("FROM Article a WHERE a.status=:articleStatus")
    Page<Article> findAllByStatus(@Param("articleStatus") ArticleStatus status,
                                  Pageable pageable);

}
