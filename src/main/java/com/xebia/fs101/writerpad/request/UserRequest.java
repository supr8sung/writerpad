package com.xebia.fs101.writerpad.request;

import com.xebia.fs101.writerpad.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequest {
    @NotBlank
    private String username;
    private String fullName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    public UserRequest(@NotBlank String username, String fullName,
                       @NotBlank @Email String email,
                       @NotBlank String password) {

        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public String getFullName() {

        return fullName;
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

    public User toUser(PasswordEncoder passwordEncoder) {

        return new User.Builder()
                .withEmail(this.getEmail())
                .withPassword(passwordEncoder.encode(getPassword()))
                .withUserName(this.getUsername()).build();
    }
}
