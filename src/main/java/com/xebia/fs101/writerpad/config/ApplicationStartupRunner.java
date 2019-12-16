package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.entity.WriterPadRole;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        User admin = userRepository.findByUsernameOrEmail("admin", "admin@writerpad.com");
        if (admin == null) {
            User user = new User.Builder().withRole(WriterPadRole.ADMIN).withPassword(
                    passwordEncoder.encode("admin@123")).withEmail(
                    "admin@writerpad.com").withUserName(
                    "admin").build();
            userRepository.save(user);
        }
    }
}
