package com.xebia.fs101.writerpad.request;

import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.Comment;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String body;

    public String getBody() {

        return body;
    }

    public CommentRequest() {

    }

    public CommentRequest(@NotBlank String body) {

        this.body = body;
    }

    public Comment toComment(Article article, String ipAdress) {

        return new Comment.Builder()
                .withBody(this.getBody())
                .withIpAddress(ipAdress)
                .withArticle(article)
                .build();
    }
}
