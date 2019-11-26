package com.xebia.fs101.writerpad.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleRequest {

    private String title;
    private String description;
    private String body;
    private List<String> tags;
    private String featuredImage;

    public ArticleRequest() {
    }

    private ArticleRequest(Builder builder) {
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
        featuredImage = builder.featuredImage;
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
        return tags;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public static final class Builder {
        private String title;
        private String description;
        private String body;
        private List<String> tags;
        private String featuredImage;

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

        public Builder withFeaturedImage(String featuredImage) {
            this.featuredImage = featuredImage;
            return this;
        }

        public ArticleRequest build() {
            return new ArticleRequest(this);
        }
    }
}
