package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @Test
    void mock_mvc_should_be_set() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_get_response() throws Exception {
        mockMvc.perform(
                post("/api/articles"))
                .andDo(print())
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    public void shouldFetchData() {
        articleRepository.count();
    }

    @Test
    void should_be_able_to_add_article_and_give_status_as_201() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("title")
                .withBody("body")
                .withDescription("description")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_give_status_bad_request_when_required_fields_are_missing_for_creating_article() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest.Builder()
                .withTitle("")
                .withDescription("description")
                .withBody("body")
                .build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_give_updated_article_after_patch_request() throws Exception {
        ArticleRequest updateArticle = new ArticleRequest.Builder()
                .withBody(" body")
                .withTitle("title")
                .withDescription("description")
                .build();
        String json = objectMapper.writeValueAsString(updateArticle);
        Article article = new Article.Builder()
                .withBody("abc")
                .withDescription("efef")
                .withTitle("fefe")
                .build();
        Article saved = articleRepository.save(article);
        String id = String.format("%s_%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(patch("/api/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.updatedAt", CoreMatchers.not(saved.getUpdatedAt())));
    }

    @Test
    public void shoulde_be_able_to_delete_an_article() throws Exception {
        Article article = new Article.Builder()
                .withBody("abc")
                .withDescription("efef")
                .withTitle("fefe")
                .build();
        Article saved = articleRepository.save(article);
        String id = String.format("%s_%s", saved.getSlug(), saved.getId());
        this.mockMvc.perform(delete("/api/articles/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void get_article_for_an_id() throws Exception {
        Article article = new Article.Builder()
                .withDescription("effe")
                .withBody("efeef")
                .withTitle("ekmfemef")
                .build();
        Article saved = articleRepository.save(article);
        String id = String.format("%s_%s", saved.getSlug(), saved.getId());
        mockMvc.perform(get("/api/articles/{slug_id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ekmfemef"))
                .andExpect(jsonPath("$.body").value("efeef"))
                .andExpect(jsonPath("$.description").value("effe"));
    }

    @Test
    void should_list_all_articles() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {
        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles?page=0&size=1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Article createArticle(String title, String body, String description) {
        Article article = new Article.Builder()
                .withTitle(title)
                .withBody(body)
                .withDescription(description)
                .build();
        return article;
    }

}