package com.xebia.fs101.writerpad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

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
    @JsonBackReference
    private List<Article> articles;
    @Enumerated(value = EnumType.STRING)
    private WriterPadRole role;

    public User() {

    }

    public User(@NotBlank String username, @NotBlank String email,
                @NotBlank String password, @NotBlank WriterPadRole role) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(User user) {

        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
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

    public WriterPadRole getRole() {

        return role;
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
        private WriterPadRole role;

        public Builder() {

        }

        public static Builder anUser() {

            return new Builder();
        }

        public Builder withRole(WriterPadRole role) {

            this.role = role;
            return this;
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

            User user = new User(userName, email, password, role);
            user.id = this.userId;
            return user;
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUsername(), getEmail(), getPassword());
    }
}
