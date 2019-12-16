package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.entity.WriterPadRole;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {

        User adminRole = userRepository.findByUsernameOrEmail("admin",
                                                              "admin@writerpad.com");
        if (adminRole == null) {
            User admin = new User.Builder().withUserName("admin").withEmail(
                    "admin@writerpad.com").withPassword(
                    passwordEncoder.encode("admin@123")).withRole(
                    WriterPadRole.ADMIN).build();
            userRepository.save(admin);
        }
    }

    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
    }

    @Test
    void should_be_able_to_register_a_user() throws Exception {

        User user = new User("supr8sung", "supreet@gmail.com", "daddy",
                             WriterPadRole.WRITER);
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("admin", "admin@123"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<User> all = userRepository.findAll();
        assertThat(all.size()).isGreaterThan(0);
    }

    //write a test for adding second user
    @Test
    void should_through_bad_request_if_user_already_registered() throws Exception {

        User user = new User("supr8sung", "supreet@gmail.com", "daddy",
                             WriterPadRole.WRITER);
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("admin", "admin@123"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("admin", "admin@123"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_return_a_user_profile() throws Exception {

        User user = new User("supr8sung", "supreet@gmail.com", "daddy",
                             WriterPadRole.WRITER);
        User savedUser = userRepository.save(user);
        this.mockMvc.perform(get("/api/profiles/{username}", savedUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("supr8sung"));
    }

    @Test
    void should_be_able_to_follow_an_user() throws Exception {
        User user1=new User("kk","kk@gmail.com",passwordEncoder.encode("abcd"),WriterPadRole.WRITER);
        User user2 =new User("ak","ak@gmail.com",passwordEncoder.encode("abcd"),WriterPadRole.WRITER);
        userRepository.saveAll(Arrays.asList(user1, user2));
        this.mockMvc.perform(post("/api/profiles/{username}/follow", "ak")
                                     .with(httpBasic("kk", "abcd")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followerCount").value(1));
        User kk = userRepository.findByUsername("kk");
        assertThat(kk.getFollowingCount()).isEqualTo(1);
        assertThat(kk.getFollowing()).isTrue();
    }

    @Test
    void should_be_able_to_unfollow_an_user() throws  Exception{
        User user1=new User("kk","kk@gmail.com",passwordEncoder.encode("abcd"),WriterPadRole.WRITER);
        User user2 =new User("ak","ak@gmail.com",passwordEncoder.encode("abcd"),WriterPadRole.WRITER);
        userRepository.save(user1);
        userRepository.save(user2);
        List<User> followers = user1.getFollowers();
        followers.add(user2);
        user2.setFollowers(followers);

        this.mockMvc.perform(delete("/api/profiles/{username}/unfollow", "ak")
                                     .with(httpBasic("kk", "abcd")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followingCount").value(0))
                .andExpect(jsonPath("$.following").value(false));


    }
}