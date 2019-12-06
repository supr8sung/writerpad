package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.response.TagsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@Transactional
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    @Query("FROM Article a WHERE a.status=:articleStatus")
    Page<Article> findAllByStatus(@Param("articleStatus") ArticleStatus status,
                                  Pageable pageable);


    String groupByQuery = "select at.tags,count(*) from " +
            "article a, article_tags at "
            + "where a.id=at.article_id group by at.tags";
    @Query(value = groupByQuery, nativeQuery = true)
    List<Object[]> findAllTags();


    @Query(value = "select tags from article_tags",nativeQuery = true)
    Stream<String> findTags();
}
