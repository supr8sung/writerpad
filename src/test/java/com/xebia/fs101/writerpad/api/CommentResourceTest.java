package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.entity.WriterPadRole;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.request.CommentRequest;
import com.xebia.fs101.writerpad.request.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@WithMockUser
class CommentResourceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User user;
    private Article savedArticle;
    String slugId = null;

    @AfterEach
    void tearDown() {

        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setup() {

        UserRequest userRequest = new UserRequest("user", "user useless", "user@mail.com",
                                                  "1234", WriterPadRole.WRITER);
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody(" body")
                .withTitle("title")
                .withDescription("description")
                .build();
        Article articleToSave = articleRequest.toArticle();
        articleToSave.setUser(user);
        savedArticle = articleRepository.save(articleToSave);
        slugId = String.format("%s_%s", savedArticle.getSlug(), savedArticle.getId());
    }

    @Test
    void should_be_able_to_post_a_comment() throws Exception {

        CommentRequest commentRequest = new CommentRequest("new comment");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments", slugId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("user","1234")))
                .andDo(print())
                .andExpect(status().isCreated());
        long count = commentRepository.count();
        assertThat(count).isGreaterThan(0);
    }

    @Test
    void should_not_be_able_to_post_a_comment_if_article_not_found() throws Exception {

        CommentRequest commentRequest = new CommentRequest("new comment");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(
                post("/api/articles/{slug_id}/comments", "abc" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).with(httpBasic("user","1234")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_post_a_comment_if_spam_found() throws Exception {

        CommentRequest commentRequest = new CommentRequest("adult");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments", slugId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("user","1234")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_delete_comment() throws Exception {

        CommentRequest commentRequest = new CommentRequest("new comment");
        Comment savedComment = commentRepository.save(
                commentRequest.toComment(savedArticle, "ipAdress"));
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{id}", slugId,
                               savedComment.getId())
        .with(httpBasic("user","1234")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_be_be_able_to_see_all_the_comments() throws Exception {

        CommentRequest commentRequest1 = new CommentRequest("first");
        CommentRequest commentRequest2 = new CommentRequest("second");
        CommentRequest commentRequest3 = new CommentRequest("third");
        commentRepository.saveAll(
                Arrays.asList(commentRequest1.toComment(savedArticle, "ip1")
                        , commentRequest2.toComment(savedArticle, "ip2")
                        , commentRequest3.toComment(savedArticle, "ip3")));
        this.mockMvc.perform(get("/api/articles/{slug_id}/comments", slugId)
        .with(httpBasic("user","1234")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}