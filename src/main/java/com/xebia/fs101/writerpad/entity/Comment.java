package com.xebia.fs101.writerpad.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String body;
    @CreationTimestamp
    private Date createAt;
    @UpdateTimestamp
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonManagedReference
    private Article article;
    private String ipAddress;

    public Comment() {

    }

    public Comment(Builder builder) {

        this.body = builder.body;
        this.createAt = new Date();
        this.updatedAt = new Date();
        this.article = builder.article;
        this.ipAddress = builder.ipAddress;
    }

    public String getIpAddress() {

        return ipAddress;
    }

    public Article getArticle() {

        return article;
    }

    public long getId() {

        return id;
    }

    public String getBody() {

        return body;
    }

    public Date getCreateAt() {

        return createAt;
    }

    public Date getUpdatedAt() {

        return updatedAt;
    }

    @Override
    public String toString() {

        return "Comment{"
                + "id="
                + id
                + ", body='"
                + body
                + '\''
                + ", createAt="
                + createAt
                + ", updatedAt="
                + updatedAt
                + '}';
    }

    public static final class Builder {
        private long id;
        private String body;
        private Date createAt;
        private Date updatedAt;
        private Article article;
        private String ipAddress;

        public Builder() {

        }

        public static Builder aComment() {

            return new Builder();
        }

        public Builder withId(long id) {

            this.id = id;
            return this;
        }

        public Builder withArticle(Article article) {

            this.article = article;
            return this;
        }

        public Builder withIpAddress(String val) {

            this.ipAddress = val;
            return this;
        }

        public Builder withBody(String body) {

            this.body = body;
            return this;
        }

        public Builder withCreateAt(Date createAt) {

            this.createAt = createAt;
            return this;
        }

        public Builder withUpdatedAt(Date updatedAt) {

            this.updatedAt = updatedAt;
            return this;
        }

        public Comment build() {

            return new Comment(this);
        }
    }
}
