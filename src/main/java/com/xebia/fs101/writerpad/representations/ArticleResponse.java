package com.xebia.fs101.writerpad.representations;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.model.ArticleStatus;

import java.util.Date;
import java.util.List;

public class ArticleResponse {
    private String id;
    private String title;
    private String slug;
    private String description;
    private String body;
    private List<String> tags;
    private Date createdAt = new Date();
    private Date updatedAt;
    private boolean favorited;
    private String image;
    private long favoritesCount;
    private UserResponse author;
    private ArticleStatus status;

    private ArticleResponse(Builder builder) {

        id = builder.id;
        title = builder.title;
        slug = builder.slug;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        favorited = builder.favorited;
        image = builder.image;
        favoritesCount = builder.favoritesCount;
        author = builder.author;
        status = builder.status;
    }

    public static ArticleResponse from(Article article) {

        return new ArticleResponse.Builder()
                .withAuthor(new UserResponse(article.getUser().getUsername()))
                .withId(article.getId().toString())
                .withBody(article.getBody())
                .withCreatedAt(article.getCreatedAt())
                .withUpdatedAt(article.getUpdatedAt())
                .withDescription(article.getDescription())
                .withSlug(article.getSlug())
                .withTags(article.getTags())
                .withTitle(article.getTitle())
                .withFavoritesCount(article.getFavoritesCount())
                .withImage(article.getImage())
                .withFavorited(article.isFavorited())
                .withStatus(article.getStatus())
                .build();
    }

    public String getId() {

        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getSlug() {

        return slug;
    }

    public String getDescription() {

        return description;
    }

    public String getBody() {

        return body;
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
    public String getImage() {
        return image;
    }

    public long getFavoritesCount() {

        return favoritesCount;
    }

    public UserResponse getAuthor() {

        return author;
    }

    public ArticleStatus getStatus() {

        return status;
    }

    private static class UserResponse {
        private String username;
         UserResponse(String username) {

            this.username = username;
        }

        public String getUsername() {

            return username;
        }
    }

    public static final class Builder {
        public ArticleStatus status;
        private String id;
        private String title;
        private String slug;
        private String description;
        private String body;
        private List<String> tags;
        private Date createdAt;
        private Date updatedAt;
        private boolean favorited;
        private long favoritesCount;
        private UserResponse author;
        private String  image;

        public Builder() {

        }
        public Builder withImage(String val) {
            this.image = val;
            return this;
        }

        public Builder withId(String val) {

            this.id = val;
            return this;
        }

        public Builder withTitle(String val) {

            title = val;
            return this;
        }

        public Builder withSlug(String val) {

            slug = val;
            return this;
        }

        public Builder withDescription(String val) {

            description = val;
            return this;
        }

        public Builder withBody(String val) {

            body = val;
            return this;
        }

        public Builder withTags(List<String> val) {

            tags = val;
            return this;
        }

        public Builder withCreatedAt(Date val) {

            createdAt = val;
            return this;
        }

        public Builder withUpdatedAt(Date val) {

            updatedAt = val;
            return this;
        }

        public Builder withFavorited(boolean val) {

            favorited = val;
            return this;
        }

        public Builder withFavoritesCount(long val) {

            favoritesCount = val;
            return this;
        }

        public Builder withAuthor(UserResponse val) {

            author = val;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {

            status = val;
            return this;
        }

        public ArticleResponse build() {

            return new ArticleResponse(this);
        }
    }
}
