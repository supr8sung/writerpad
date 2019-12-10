package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
    }

    @Test
    void should_be_able_to_register_a_user() throws Exception {

        User user = new User("supr8sung", "supreet@gmail.com", "daddy");
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated());
        List<User> all = userRepository.findAll();
        Assertions.assertThat(all.size()).isGreaterThan(0);
    }
    //write a test for adding second user
    @Test
    void should_through_bad_request_if_user_already_registered() throws Exception {
        User user = new User("supr8sung", "supreet@gmail.com", "daddy");
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}