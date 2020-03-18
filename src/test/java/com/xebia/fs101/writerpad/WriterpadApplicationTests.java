package com.xebia.fs101.writerpad;

import com.xebia.fs101.writerpad.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WriterpadApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }
    @Test
    void contextLaod() {

    }
}
