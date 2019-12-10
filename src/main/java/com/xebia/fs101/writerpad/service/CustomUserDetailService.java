package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail,
                                                         usernameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist");
        }
        return new CustomUserDetails(user);
    }
}
