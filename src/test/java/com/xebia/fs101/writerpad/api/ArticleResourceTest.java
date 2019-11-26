package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.model.ArticleRequest;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
class ArticleResourceTest {

    @Mock
    HttpServletResponse httpServletResponse;
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
                        MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void should_be_able_to_fetch_data() {
        assertThat(articleRepository).isNotNull();
    }


    @Test
    public void should_be_able_to_add_data() throws Exception {
        assertThat(articleRepository);
        ArticleRequest articleRequest = new ArticleRequest.Builder().
                withTitle("junit").
                withDescription("used for testing").
                withBody("it is a library").
                withTags(Arrays.asList("abc,def")).
                build();
        long countBeforeAdd = articleRepository.count();
        articleResource.create(articleRequest, httpServletResponse);
        long countAfterAdd = articleRepository.count();
        assertThat(countAfterAdd - countBeforeAdd).isEqualTo(1);

    }

}