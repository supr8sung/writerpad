package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.request.CommentRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentResourceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private Article savedArticle;
    String slugId = null;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withBody(" body")
                .withTitle("title")
                .withDescription("description")
                .build();
        savedArticle = articleRepository.save(articleRequest.toArticle());
        slugId = String.format("%s_%s", savedArticle.getSlug(), savedArticle.getId());
    }

    @Test
    void should_be_able_to_post_a_comment() throws Exception {
        CommentRequest commentRequest = new CommentRequest("new comment");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments", slugId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_post_a_comment_if_spam_found() throws Exception {
        CommentRequest commentRequest = new CommentRequest("adult");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slug_id}/comments", slugId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_delete_comment() throws Exception {
        CommentRequest commentRequest = new CommentRequest("new comment");
        Comment savedComment = commentRepository.save(commentRequest.toComment(savedArticle, "ipAdress"));
        mockMvc.perform(delete("/api/articles/{slug_id}/comments/{id}", slugId, savedComment.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void shoulde_be_able_to_see_all_the_comments() throws Exception {
        CommentRequest commentRequest1 = new CommentRequest("first");
        CommentRequest commentRequest2 = new CommentRequest("second");
        CommentRequest commentRequest3 = new CommentRequest("third");
        commentRepository.saveAll(Arrays.asList(commentRequest1.toComment(savedArticle, "ip1")
                , commentRequest2.toComment(savedArticle, "ip2")
                , commentRequest3.toComment(savedArticle, "ip3")));
        this.mockMvc.perform(get("/api/articles/{slug_id}/comments", slugId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

}