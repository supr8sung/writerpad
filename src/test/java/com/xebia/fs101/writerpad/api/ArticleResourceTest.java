package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
class ArticleResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleResource articleResource;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void mock_mvc_should_be_set() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_get_response() throws Exception {
        mockMvc.perform(
                post("/api/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                        status().isBadRequest());

    }

    @Test
    public void shouldFetchData() {
        articleRepository.count();
    }


    @Test
    void should_be_able_to_add_article_and_give_status_as_201() throws Exception {
        String json="{\n" +
                "  \"title\": \"How to learn Spring Booot\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    void should_give_status_bad_request() throws Exception {
        String json="{\n" +
                "  \"title\": \"\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



}