package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
