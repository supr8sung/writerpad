package com.xebia.fs101.writerpad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.fs101.writerpad.model.ArticleStatus;
import com.xebia.fs101.writerpad.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Entity
//@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Transient
    private String slug;
    private String title;
    private String description;
    @Column(name = "body", length = 5555)
    private String body;
    @ElementCollection
    private List<String> tags;
    @Column(updatable = false, nullable = false)
    private Date createdAt;
    private Date updatedAt;
    private boolean favorited;
    private long favoritesCount;
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;
    @JsonBackReference
    @OneToMany(mappedBy = "article")
    private List<Comment> comment;
    @ManyToOne(optional = false)
    @JsonManagedReference
    private User user;

    public Article() {

    }

    public Article(Builder builder) {

        id = builder.id;
        tags = builder.tags;
        title = builder.title;
        body = builder.body;
        createdAt = new Date();
        updatedAt = new Date();
        favoritesCount = builder.favoritesCount;
        favorited = builder.favorited;
        description = builder.description;
        slug = StringUtils.slugify(this.title);
        status = ArticleStatus.DRAFT;
    }

    public UUID getId() {

        return id;
    }

    public String getSlug() {

        return StringUtils.slugify(this.title);
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public ArticleStatus getStatus() {

        return status;
    }

    public List<Comment> getComment() {

        return comment;
    }

    public String getDescription() {

        return description;
    }

    public String getBody() {

        return body;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public List<String> getTags() {

        return tags;
    }

    public Date getCreatedAt() {

        return createdAt;
    }

    public Date getUpdatedAt() {

        return updatedAt;
    }

    public boolean isFavorited() {

        return favorited;
    }

    public void setFavorited(boolean favorited) {

        this.favorited = favorited;
    }

    public long getFavoritesCount() {

        return favoritesCount;
    }

    public void setFavoritesCount(long favoritesCount) {

        this.favoritesCount = favoritesCount;
    }

    public Article update(Article changedArticle) {

        if (Objects.nonNull(changedArticle.getTitle())) {
            this.title = changedArticle.getTitle();
        }
        if (Objects.nonNull(changedArticle.getBody())) {
            this.body = changedArticle.getBody();
        }
        if (Objects.nonNull(changedArticle.getDescription())) {
            this.description = changedArticle.getDescription();
        }
        if (Objects.nonNull(
                changedArticle.getTags()) && changedArticle.getTags().size() > 0) {
            this.tags = changedArticle.getTags();
        }
        this.updatedAt = new Date();
        return this;
    }

    public Article publish() {

        this.status = ArticleStatus.PUBLISHED;
        return this;
    }

    public static final class Builder {
        public ArticleStatus status;
        private UUID id;
        private String slug;
        private String title;
        private String description;
        private String body;
        private List<String> tags;
        private Date createdAt;
        private Date updatedAt;
        private boolean favorited = false;
        private long favoritesCount = 0;

        public Builder() {

        }

        public Builder withId(UUID id) {

            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {

            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {

            this.description = description;
            return this;
        }

        public Builder withBody(String body) {

            this.body = body;
            return this;
        }

        public Builder withTags(List<String> tags) {

            this.tags = tags == null ? new ArrayList<>() : tags.stream().map(
                    StringUtils::slugify).collect(toList());
            return this;
        }

        public Builder withFavorited(boolean favorited) {

            this.favorited = favorited;
            return this;
        }

        public Builder withFavoritesCount(long favoritesCount) {

            this.favoritesCount = favoritesCount;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {

            this.status = val;
            return this;
        }

        public Article build() {

            return new Article(this);
        }
    }
}
