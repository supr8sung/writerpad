package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.representations.UserResponse;
import com.xebia.fs101.writerpad.request.UserRequest;
import com.xebia.fs101.writerpad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class UserResource {
    @Autowired
    private UserService userService;

    // @AdminOnly
    @PostMapping(path = "/api/users")
    public ResponseEntity register(@Valid @RequestBody UserRequest userRequest) {

        userService.register(userRequest);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping(path = "/api/profiles/{username}")
    public ResponseEntity<UserResponse> get(@PathVariable(value = "username") String username) {

        User user = userService.findUser(username);
        return new ResponseEntity<>(UserResponse.from(user), OK);
    }

    @PostMapping(path = "/api/profiles/{username}/follow")
    public ResponseEntity<UserResponse> follow(@AuthenticationPrincipal User user
            , @PathVariable(value = "username") String username) {

        if (username.equals(user.getUsername()))
            return ResponseEntity.status(BAD_REQUEST).build();
        User userToBeFollowed = userService.follow(user, username);
        if (Objects.isNull(userToBeFollowed))
            return ResponseEntity.status(NOT_FOUND).build();
        return new ResponseEntity<>(UserResponse.from(userToBeFollowed), OK);
    }

    @DeleteMapping(path = "/api/profiles/{username}/unfollow")
    public ResponseEntity<UserResponse> unfollow(@AuthenticationPrincipal User user
            , @PathVariable(value = "username") String username) {

        if (username.equals(user.getUsername()))
            return ResponseEntity.status(BAD_REQUEST).build();
        User userToBeUnfollowed = userService.unfollow(user, username);
        if (Objects.isNull(userToBeUnfollowed))
            return ResponseEntity.status(NOT_FOUND).build();
        return new ResponseEntity<>(UserResponse.from(userToBeUnfollowed), OK);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void badRequest() {

    }
}
