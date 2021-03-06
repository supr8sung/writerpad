package com.xebia.fs101.writerpad.request;

import com.xebia.fs101.writerpad.entity.Article;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleRequest {
    @NotNull
    @NotEmpty
    private String title;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    @NotEmpty
    private String body;
    private List<String> tags;

    public ArticleRequest() {

    }

    private ArticleRequest(Builder builder) {

        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
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

    public List<String> getTags() {

        return tags == null ? new ArrayList<String>() : tags;
    }

    public Article toArticle() {

        return new Article.Builder()
                .withBody(this.getBody())
                .withTitle(this.getTitle())
                .withDescription(this.getDescription())
                .withTags(this.tags == null ? new ArrayList<>() : this.tags.stream()
                        .map(e -> e.replaceAll(" ", "-").toLowerCase())
                        .collect(Collectors.toList()))
                .build();
    }

    public static final class Builder {
        private String title;
        private String description;
        private String body;
        private List<String> tags;

        public Builder() {

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

            this.tags = tags;
            return this;
        }

        public ArticleRequest build() {

            return new ArticleRequest(this);
        }
    }
}
