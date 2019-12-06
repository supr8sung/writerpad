package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.xebia.fs101.writerpad.model.ArticleStatus.DRAFT;
import static com.xebia.fs101.writerpad.model.ArticleStatus.PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleResourceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;

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

        mockMvc.perform(post("/api/articles")).andDo(print()).andExpect(
                status().isBadRequest());
    }

    @Test
    void should_be_able_to_add_article_and_give_status_as_201() throws Exception {

        ArticleRequest articleRequest =
                new ArticleRequest.Builder().withTitle("title").withBody(
                        "body").withDescription("description").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles").accept(MediaType.APPLICATION_JSON).content(
                json).contentType(MediaType.APPLICATION_JSON)).andExpect(
                status().isCreated());
    }

    @Test
    void should_give_status_bad_request_when_required_fields_are_missing_for_creating_article() throws Exception {

        ArticleRequest articleRequest =
                new ArticleRequest.Builder().withTitle("").withDescription(
                        "description").withBody("body").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles").accept(MediaType.APPLICATION_JSON).content(
                json).contentType(MediaType.APPLICATION_JSON)).andExpect(
                status().isBadRequest());
    }

    @Test
    void should_give_updated_article_after_patch_request() throws Exception {

        ArticleRequest updateArticle =
                new ArticleRequest.Builder().withBody(" body").withTitle(
                        "title").withDescription("description").build();
        String json = objectMapper.writeValueAsString(updateArticle);
        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                patch("/api/articles/{id}", slugIdGenerator.apply(saved)).contentType(
                        MediaType.APPLICATION_JSON).content(json)).andDo(
                print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.title").value("title")).andExpect(
                jsonPath("$.updatedAt", CoreMatchers.not(saved.getUpdatedAt())));
    }

    @Test
    public void shoulde_be_able_to_delete_an_article() throws Exception {

        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                delete("/api/articles/{id}", slugIdGenerator.apply(saved))).andDo(
                print()).andExpect(status().isNoContent());
    }

    @Test
    void get_article_for_an_id() throws Exception {

        Article article = new Article.Builder().withDescription("effe").withBody(
                "efeef").withTitle("ekmfemef").build();
        Article saved = articleRepository.save(article);
        mockMvc.perform(
                get("/api/articles/{slug_id}", slugIdGenerator.apply(saved))).andDo(
                print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.title").value("ekmfemef")).andExpect(
                jsonPath("$.body").value("efeef")).andExpect(
                jsonPath("$.description").value("effe"));
    }

    @Test
    void should_list_all_articles() throws Exception {

        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")).andDo(
                MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.length()").value(3));
    }

    @Test
    void should_return_all_articles_according_to_status() throws Exception {

        Article article1 = createArticle("title1", "body1", "description1");
        Article article2 = createArticle("title2", "body2", "description2");
        Article article3 = createArticle("title3", "body3", "description3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles/?status=DRAFT")).andDo(print()).andExpect(
                jsonPath("$.length()").value(3));
        articleService.publish(article2);
        this.mockMvc.perform(get("/api/articles/?status=DRAFT")).andDo(print()).andExpect(
                jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_all_articles_according_to_status_and_support_pagination() throws Exception {

        Article article1 = createArticle("title1", "body1", "description1");
        Article article2 = createArticle("title2", "body2", "description2");
        Article article3 = createArticle("title3", "body3", "description3");
        Article article4 = createArticle("title4", "body4", "description5");
        Article article5 = createArticle("title4", "body4", "description5");
        articleRepository.saveAll(
                Arrays.asList(article1, article2, article3, article4, article5));
        articleService.publish(article2);
        articleService.publish(article3);
        this.mockMvc.perform(get("/api/articles/?status=DRAFT&size=2")).andDo(
                print()).andExpect(jsonPath("$.length()"
        ).value(2));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {

        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/articles?page=0&size=1")).andDo(
                MockMvcResultHandlers.print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.length()").value(1));
    }

    @Test
    void should_show_status_as_draft_for_newly_created_articles() throws Exception {

        Article article = createArticle("title", "body", "description");
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(
                get("/api/articles/{slugId}", slugIdGenerator.apply(savedArticle))).andDo(
                print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.title").value("title")).andExpect(
                jsonPath("$.status").value(DRAFT.toString()));
    }

    @Test
    void should_be_able_to_publish_a_article() throws Exception {

        Article article = createArticle("title", "body", "description");
        Article savedArticle = articleRepository.save(article);
        assertThat(savedArticle.getStatus()).isEqualByComparingTo(DRAFT);
        this.mockMvc.perform(post("/api/articles/{slugId}/{status}",
                                  slugIdGenerator.apply(savedArticle), PUBLISHED)).andDo(
                print()).andExpect(status().isNoContent());
    }

    @Test
    void should_give_400_if_blog_already_published() throws Exception {

        Article article = createArticle("title", "body", "description");
        Article savedArticle = articleRepository.save(article);
        assertThat(savedArticle.getStatus()).isEqualByComparingTo(DRAFT);
        savedArticle.publish();
        Article save = articleRepository.save(savedArticle);
        this.mockMvc.perform(
                post("/api/articles/{slugId}/{status}", slugIdGenerator.apply(save),
                     PUBLISHED)).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void should_give_reading_time_for_any_article() throws Exception {


        String body= IntStream.range(1, 400)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        System.out.println(body);
        Article article=createArticle("title",body,"desc");

        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(get("/api/articles/{slug_id}/timetoread",
                                 slugIdGenerator.apply(savedArticle)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.slugId").value(slugIdGenerator.apply(savedArticle)))
                .andExpect(jsonPath("$.readingTime").isNotEmpty())
                .andExpect(jsonPath("$.readingTime.mins").value(1))
                .andExpect(jsonPath("$.readingTime.seconds").value(46));
    }

    @Test
    void should_give_all_the_tags_with_their_respective_count() throws Exception {

        ArticleRequest articleRequest1 = new ArticleRequest.Builder()
                .withBody("body1").withTitle("title1").withDescription("desc1").withTags(
                        Arrays.asList("java")).build();
        ArticleRequest articleRequest2 = new ArticleRequest.Builder()
                .withBody("body2").withTitle("title2").withDescription("desc2").withTags(
                        Arrays.asList("python", "Java", "jAVa")).build();
        Article article1 = articleRequest1.toArticle();
        Article article2 = articleRequest2.toArticle();
        articleRepository.saveAll(Arrays.asList(article1, article2));
        this.mockMvc.perform(get("/api/articles/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].tag").hasJsonPath())
                .andExpect(jsonPath("$.[0].tag").value("python"))
                .andExpect(jsonPath("$.[1].occurence").value("3"));
    }

    @Test
    void should_be_able_to_mark_an_article_as_favourite() throws Exception {

        Article article = createArticle("title", "body", "desc");
        article.setFavoritesCount(4L);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(put("/api/articles/{slug_id}/favourite",
                                 slugIdGenerator.apply(savedArticle)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_be_able_to_mark_an_article_as_unfavourite() throws Exception {

        Article article = createArticle("title", "body", "desc");
        article.setFavoritesCount(4L);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(delete("/api/articles/{slug_id}/unfavourite",
                                    slugIdGenerator.apply(savedArticle)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(savedArticle.getFavoritesCount()).isEqualTo(3L);
        Optional<Article> article1 = articleRepository.findById(savedArticle.getId());
        assertThat(article1.get().getFavoritesCount()).isEqualTo(3L);


    }

    private Article createArticle(String title, String body, String description) {

        Article article = new Article.Builder().withTitle(title).withBody(
                body).withDescription(description).build();
        return article;
    }

    Function<Article, String> slugIdGenerator = (article) -> String.format("%s_%s",
                                                                           article.getSlug(),
                                                                           article.getId());
}