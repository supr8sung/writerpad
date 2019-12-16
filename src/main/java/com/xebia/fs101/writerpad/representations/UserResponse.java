package com.xebia.fs101.writerpad.representations;

import com.xebia.fs101.writerpad.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserResponse {
    private String userName;
    private boolean following;
    private long followerCount;
    private long followingCount;
    private List<ArticleResponse> articles;

    private static class ArticleResponse {
        private UUID id;
        private String title;

         ArticleResponse(UUID id, String title) {

            this.id = id;
            this.title = title;
        }

        public UUID getId() {

            return id;
        }

        public String getTitle() {

            return title;
        }
    }

    public void setFollowing(boolean following) {

        this.following = following;
    }

    public void setFollowerCount(long followerCount) {

        this.followerCount = followerCount;
    }

    public void setFollowingCount(long followingCount) {

        this.followingCount = followingCount;
    }

    public String getUserName() {

        return userName;
    }

    public boolean isFollowing() {

        return following;
    }

    public long getFollowerCount() {

        return followerCount;
    }

    public long getFollowingCount() {

        return followingCount;
    }

    public List<ArticleResponse> getArticles() {

        return articles;
    }

    private UserResponse(Builder builder) {

        this.userName = builder.userName;
        this.following = builder.following;
        this.followerCount = builder.followerCount;
        this.followingCount = builder.followingCount;
        this.articles = builder.articles;
    }

    private static final class Builder {
        private String userName;
        private boolean following;
        private long followerCount;
        private long followingCount;
        private List<ArticleResponse> articles;

         Builder() {

        }
//        public static Builder anUserResponse() {
//
//            return new Builder();
//        }

        public Builder withUserName(String userName) {

            this.userName = userName;
            return this;
        }

        public Builder withFollowing(boolean following) {

            this.following = following;
            return this;
        }

        public Builder withFollowerCount(long followerCount) {

            this.followerCount = followerCount;
            return this;
        }

        public Builder withFollowingCount(long followingCount) {

            this.followingCount = followingCount;
            return this;
        }

        public Builder withArticles(List<ArticleResponse> articles) {

            this.articles = articles == null ? new ArrayList<>() : articles;
            return this;
        }

        public UserResponse build() {

            return new UserResponse(this);
        }
    }

    public static UserResponse from(User user) {

        return new UserResponse.Builder()
                .withUserName(user.getUsername())
                .withArticles(user.getArticles().stream()
                                      .map(e -> new ArticleResponse(e.getId(), e.getTitle()))
                                      .collect(Collectors.toList()))
                .withFollowerCount(user.getFollowerCount())
                .withFollowingCount(user.getFollowingCount())
                .withFollowing(user.getFollowing())
                .build();
    }
}
