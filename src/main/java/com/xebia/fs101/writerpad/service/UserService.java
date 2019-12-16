package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User register(UserRequest userRequest) {

        User user = userRequest.toUser(passwordEncoder);
        return userRepository.save(user);
    }

    public User findUser(String username) {

        return userRepository.findByUsername(username);
    }

    public User follow(User user, String username) {

        User followedUser = userRepository.findByUsername(username);
        if (Objects.isNull(followedUser))
            return null;
        User followingUser = userRepository.findByUsername(user.getUsername());
        if (isFollower(followingUser, followedUser))
            return followedUser;
        followingUser.setFollowing(true);
        followingUser.setFollowingCount(followingUser.getFollowingCount() + 1);
        userRepository.save(followingUser);
        List<User> followers = followedUser.getFollowers();
        followers.add(followingUser);
        followedUser.setFollowers(followers);
        followedUser.setFollowerCount(followedUser.getFollowerCount() + 1);
        return userRepository.save(followedUser);
    }

    public User unfollow(User user, String username) {

        User unfollowedUser = userRepository.findByUsername(username);
        if (Objects.isNull(unfollowedUser))
            return null;
        User unfollowingUser = userRepository.findByUsername(user.getUsername());
        if (!isFollower(unfollowingUser, unfollowedUser))
            return unfollowedUser;
        unfollowingUser.setFollowingCount(unfollowingUser.getFollowingCount() - 1);
        if (unfollowingUser.getFollowingCount() == 0)
            unfollowingUser.setFollowing(false);
        userRepository.save(unfollowingUser);
        List<User> followers = unfollowedUser.getFollowers();
        followers.remove(unfollowingUser);
        unfollowedUser.setFollowers(followers);
        unfollowedUser.setFollowerCount(unfollowedUser.getFollowerCount() - 1);
        return userRepository.save(unfollowedUser);
    }

    private boolean isFollower(User followingUser, User followedUser) {

        return followedUser.getFollowers().stream().anyMatch(
                e -> e.getUsername().equals(followingUser.getUsername()));
    }
}
