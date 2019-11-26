package com.xebia.fs101.writerpad.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    private String slug;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String body;

    @ElementCollection
    private List<String> tagList = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
    private boolean favourited = false;
    private  int favouriteCount = 0;




    public Article(Builder builder) {
        id = builder.id;
        tagList = builder.tagList;
        title = builder.title;
        body = builder.body;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        favouriteCount = builder.favouriteCount;
        favourited = builder.favourited;
        description = builder.description;
        slug = builder.slug;

    }


    public static final class Builder {
        private UUID id;
        private String slug;
        private String title;
        private String description;
        private String body;
        private List<String> tagList;
        private Date createdAt;
        private Date updatedAt;
        private boolean favourited = false;
        private  int favouriteCount = 0;

        public Builder() {
        }


        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withSlug(String slug) {
            this.slug = slug;
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

        public Builder withTagList(List<String> tagList) {
            this.tagList = tagList;
            return this;
        }

        public Builder withCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder withFavourited(boolean favourited) {
            this.favourited = favourited;
            return this;
        }

        public Builder withFavouriteCount(int favouriteCount) {
            this.favouriteCount = favouriteCount;
            return this;
        }

        public Article build() {

            return new Article(this);
        }
    }
}
