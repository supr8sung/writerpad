package com.xebia.fs101.writerpad.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    public User() {

    }

    public User(@NotBlank String username, @NotBlank String email,
                @NotBlank String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(User user) {

        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getUsername() {

        return username;
    }

    public String getEmail() {

        return email;
    }

    public String getPassword() {

        return password;
    }

    public Long getId() {

        return id;
    }

    @Override
    public String toString() {

        return "User{"
                + "userId=" + id
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }

    public static final class Builder {
        private Long userId;
        private String userName;
        private String email;
        private String password;

        public Builder() {

        }

        public static Builder anUser() {

            return new Builder();
        }

        public Builder withUserId(Long userId) {

            this.userId = userId;
            return this;
        }

        public Builder withUserName(String userName) {

            this.userName = userName;
            return this;
        }

        public Builder withEmail(String email) {

            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {

            this.password = password;
            return this;
        }

        public User build() {

            User user = new User(userName, email, password);
            user.id = this.userId;
            return user;
        }
    }
}
