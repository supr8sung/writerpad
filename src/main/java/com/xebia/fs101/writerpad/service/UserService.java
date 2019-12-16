package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private User getUser(String username) {

        User byUsername = userRepository.findByUsername(username);
        if (Objects.isNull(byUsername))
            throw new UsernameNotFoundException("Can't find the use to be followed");
        return byUsername;
    }

    public User follow(User user, String username) {

        User followedUser = getUser(username);
        User followingUser = getUser(user.getUsername());
        if (followedUser.getFollowers().contains(followingUser.getUsername()))
            return followedUser;
        followingUser.setFollowing(true);
        followingUser.setFollowingCount(followingUser.getFollowingCount() + 1);
        userRepository.save(followingUser);
        List<String> followers = followedUser.getFollowers();
        followers.add(followingUser.getUsername());
        followedUser.setFollowers(followers);
        followedUser.setFollowerCount(followedUser.getFollowerCount() + 1);
        return userRepository.save(followedUser);
    }

    public User unfollow(User user, String username) {

        User unfollowedUser = getUser(username);
        User unfollowingUser = getUser(user.getUsername());
        if (!unfollowedUser.getFollowers().contains(unfollowingUser.getUsername()))
            return unfollowedUser;
        unfollowingUser.setFollowingCount(unfollowingUser.getFollowingCount() - 1);
        if (unfollowingUser.getFollowingCount() == 0)
            unfollowingUser.setFollowing(false);
        userRepository.save(unfollowingUser);
        List<String> followers = unfollowedUser.getFollowers();
        followers.remove(unfollowingUser.getUsername());
        unfollowedUser.setFollowers(followers);
        unfollowedUser.setFollowerCount(unfollowedUser.getFollowerCount() - 1);
        return userRepository.save(unfollowedUser);
    }
}
