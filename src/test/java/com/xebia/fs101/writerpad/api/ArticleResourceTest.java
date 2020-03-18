package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.entity.Article;
import com.xebia.fs101.writerpad.entity.User;
import com.xebia.fs101.writerpad.entity.WriterPadRole;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.request.UserRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {

        UserRequest userRequest = new UserRequest("user", "user useless", "user@mail.com",
                                                  "1234", WriterPadRole.WRITER);
        user = userRequest.toUser(passwordEncoder);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {

        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_get_response() throws Exception {

        mockMvc.perform(post("/api/articles")
                                .accept(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print()).andExpect(
                status().isBadRequest());
    }

    @Test
    void should_be_able_to_add_article_and_give_status_as_201() throws Exception {

        User admin = new User.Builder().withRole(WriterPadRole.ADMIN).withUserName(
                "supr8sung").withPassword(passwordEncoder.encode("1234"))
                .withEmail("supreet@gmail.com").build();
        userRepository.save(admin);
        ArticleRequest articleRequest =
                new ArticleRequest.Builder().withTitle("title").withBody(
                        "body").withDescription("description").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles")

                                .accept(MediaType.APPLICATION_JSON)
                                .content(json).with(httpBasic("supr8sung", "1234"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_give_status_bad_request_when_required_fields_are_missing_for_creating_article() throws Exception {

        ArticleRequest articleRequest =
                new ArticleRequest.Builder().withTitle("").withDescription(
                        "description").withBody("body").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        mockMvc.perform(post("/api/articles").accept(MediaType.APPLICATION_JSON).content(
                json).with(httpBasic("user", "1234")).contentType(
                MediaType.APPLICATION_JSON)).andExpect(
                status().isBadRequest());
    }

    @Test
    void should_be_able_to_update_an_article() throws Exception {

        ArticleRequest updateArticle =
                new ArticleRequest.Builder().withBody(" body").withTitle(
                        "title").withDescription("description").build();
        String json = objectMapper.writeValueAsString(updateArticle);
        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                patch("/api/articles/{id}", slugIdGenerator.apply(saved)).contentType(
                        MediaType.APPLICATION_JSON).content(json).with(
                        httpBasic("user", "1234"))).andDo(
                print()).andExpect(status().isOk()).andExpect(
                jsonPath("$.title").value("title")).andExpect(
                jsonPath("$.updatedAt", CoreMatchers.not(saved.getUpdatedAt())));
    }

    @Test
    public void shoulde_be_able_to_delete_an_article() throws Exception {

        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        User user1 = new User.Builder().withUserName("user1").withEmail(
                "user1@gmail.com").withPassword(passwordEncoder.encode("1234")).withRole(
                WriterPadRole.ADMIN).build();
        User editor = userRepository.save(user1);
        article.setUser(editor);
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                delete("/api/articles/{id}", slugIdGenerator.apply(saved))
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user1", "1234")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void get_article_for_an_id() throws Exception {

        Article article = new Article.Builder().withDescription("effe").withBody(
                "efeef").withTitle("ekmfemef").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        mockMvc.perform(
                get("/api/articles/{slug_id}", slugIdGenerator.apply(saved))
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ekmfemef"))
                .andExpect(jsonPath("$.body").value("efeef"))
                .andExpect(jsonPath("$.description").value("effe"));
    }

    @Test
    void should_list_all_articles() throws Exception {

        Article article1 = createArticle("Title1", "Description1", "body1");
        article1.setUser(user);
        Article article2 = createArticle("Title2", "description2", "body2");
        article2.setUser(user);
        Article article3 = createArticle("Title3", "description3", "body3");
        article3.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/articles")
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(MockMvcResultHandlers.print()).andExpect(
                status().isOk()).andExpect(
                jsonPath("$.length()").value(3));
    }

    @Test
    void should_return_all_articles_according_to_status() throws Exception {

        Article article1 = createArticle("title1", "body1", "description1");
        article1.setUser(user);
        Article article2 = createArticle("title2", "body2", "description2");
        article2.setUser(user);
        Article article3 = createArticle("title3", "body3", "description3");
        article3.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(get("/api/articles/?status=DRAFT")
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(3));
        articleService.publish(article2);
        this.mockMvc.perform(get("/api/articles/?status=DRAFT")
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_all_articles_according_to_status_and_support_pagination() throws Exception {

        Article article1 = createArticle("title1", "body1", "description1");
        article1.setUser(user);
        Article article2 = createArticle("title2", "body2", "description2");
        article2.setUser(user);
        Article article3 = createArticle("title3", "body3", "description3");
        article3.setUser(user);
        Article article4 = createArticle("title4", "body4", "description5");
        article4.setUser(user);
        Article article5 = createArticle("title4", "body4", "description5");
        article5.setUser(user);
        articleRepository.saveAll(
                Arrays.asList(article1, article2, article3, article4, article5));
        articleService.publish(article2);
        articleService.publish(article3);
        this.mockMvc.perform(get("/api/articles/?status=DRAFT&size=2")
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_list_all_articles_with_pagination() throws Exception {

        Article article1 = createArticle("Title1", "Description1", "body1");
        Article article2 = createArticle("Title2", "description2", "body2");
        Article article3 = createArticle("Title3", "description3", "body3");
        article1.setUser(user);
        article2.setUser(user);
        article3.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2, article3));
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/articles?page=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(
                        MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void should_show_status_as_draft_for_newly_created_articles() throws Exception {

        Article article = createArticle("title", "body", "description");
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(
                get("/api/articles/{slugId}", slugIdGenerator.apply(savedArticle))
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.status").value(DRAFT.toString()));
    }

    @Test
    void should_be_able_to_publish_a_article() throws Exception {

        Article article = createArticle("title", "body", "description");
        User user1 = new User.Builder().withUserName("user1").withEmail(
                "user1@gmail.com").withPassword(passwordEncoder.encode("1234")).withRole(
                WriterPadRole.EDITOR).build();
        User editor = userRepository.save(user1);
        article.setUser(editor);
        Article savedArticle = articleRepository.save(article);
        assertThat(savedArticle.getStatus()).isEqualByComparingTo(DRAFT);
        this.mockMvc.perform(post("/api/articles/{slugId}/{status}",
                                  slugIdGenerator.apply(savedArticle), PUBLISHED)
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user1", "1234")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_give_400_if_blog_already_published() throws Exception {

        Article article = createArticle("title", "body", "description");
        User user1 = new User.Builder().withUserName("user1").withEmail(
                "user1@gmail.com").withPassword(passwordEncoder.encode("1234")).withRole(
                WriterPadRole.EDITOR).build();
        User editor = userRepository.save(user1);
        article.setUser(editor);
        Article savedArticle = articleRepository.save(article);
        assertThat(savedArticle.getStatus()).isEqualByComparingTo(DRAFT);
        savedArticle.publish();
        Article save = articleRepository.save(savedArticle);
        this.mockMvc.perform(
                post("/api/articles/{slugId}/{status}", slugIdGenerator.apply(save),
                     PUBLISHED)
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user1", "1234")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void should_give_reading_time_for_any_article() throws Exception {

        String body = IntStream.range(1, 400)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        Article article = createArticle("title", body, "desc");
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(get("/api/articles/{slug_id}/timetoread",
                                 slugIdGenerator.apply(savedArticle))
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
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
        article1.setUser(user);
        article2.setUser(user);
        articleRepository.saveAll(Arrays.asList(article1, article2));
        this.mockMvc.perform(get("/api/articles/tags")
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
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
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(put("/api/articles/{slug_id}/favourite",
                                 slugIdGenerator.apply(savedArticle))
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void should_be_able_to_mark_an_article_as_unfavourite() throws Exception {

        Article article = createArticle("title", "body", "desc");
        article.setFavoritesCount(4L);
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);
        this.mockMvc.perform(delete("/api/articles/{slug_id}/unfavourite",
                                    slugIdGenerator.apply(savedArticle))
                                     .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(status().isNoContent());
        Optional<Article> article1 = articleRepository.findById(savedArticle.getId());
        assertThat(article1.get().getFavoritesCount()).isEqualTo(3L);
    }

    @Test
    void should_be_able_to_update_an_article_by_authorized_user() throws Exception {

        UserRequest userRequest1 = new UserRequest("user1", "user useless",
                                                   "user1@gmail.com",
                                                   "1234", WriterPadRole.WRITER);
        UserRequest userRequest2 = new UserRequest("user2", "user useless",
                                                   "user2@gmail.com",
                                                   "1234", WriterPadRole.WRITER);
        User user1 = userRequest1.toUser(passwordEncoder);
        User user2 = userRequest2.toUser(passwordEncoder);
        userRepository.saveAll(Arrays.asList(user1, user2));
        Article article = new Article.Builder().withBody("old Body").withDescription(
                "old desc").withTitle("old title").build();
        article.setUser(user1);
        Article savedArticle = articleRepository.save(article);
        ArticleRequest updatedArticleRequest =
                new ArticleRequest.Builder().withBody("new body").withTitle(
                        "new title").withDescription("new desc").build();
        String json = objectMapper.writeValueAsString(updatedArticleRequest);
        this.mockMvc.perform(
                patch("/api/articles/{id}",
                      slugIdGenerator.apply(savedArticle)).contentType(
                        MediaType.APPLICATION_JSON).content(json).with(
                        httpBasic("user1", "1234"))).andDo(
                print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.body").value("new body"))
                .andExpect(jsonPath("$.updatedAt",
                                    CoreMatchers.not(savedArticle.getUpdatedAt())));
    }

    @Test
    void should_not_update_an_article_by_unauthorized_user() throws Exception {

        UserRequest userRequest1 = new UserRequest("user1", "user useless",
                                                   "user1@gmail.com",
                                                   "1234", WriterPadRole.WRITER);
        UserRequest userRequest2 = new UserRequest("user2", "user useless",
                                                   "user2@gmail.com",
                                                   "1234", WriterPadRole.WRITER);
        User user1 = userRequest1.toUser(passwordEncoder);
        User user2 = userRequest2.toUser(passwordEncoder);
        userRepository.saveAll(Arrays.asList(user1, user2));
        Article article = new Article.Builder().withBody("old Body").withDescription(
                "old desc").withTitle("old title").build();
        article.setUser(user1);
        Article savedArticle = articleRepository.save(article);
        ArticleRequest updatedArticleRequest =
                new ArticleRequest.Builder().withBody("new body").withTitle(
                        "new title").withDescription("new desc").build();
        String json = objectMapper.writeValueAsString(updatedArticleRequest);
        this.mockMvc.perform(
                patch("/api/articles/{id}",
                      slugIdGenerator.apply(savedArticle)).contentType(
                        MediaType.APPLICATION_JSON).content(json).with(
                        httpBasic("user2", "1234"))).andDo(
                print()).andExpect(status().isUnauthorized());
    }

    @Test
    void should_not_delete_article_if_user_not_authorized() throws Exception {

        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        article.setUser(user);
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                delete("/api/articles/{id}", slugIdGenerator.apply(saved))
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user1", "1234")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_not_post_article_if_similar_article_is_already_registered() throws Exception {

        Article article = createArticle("title", "body", "desc");
        article.setUser(user);
        articleRepository.save(article);
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(post("/api/articles")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(json)
                                     .with(httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_article_if_same_user_is_updating_it_without_checking_for_plagiarism() throws Exception {

        Article article = createArticle("title", "body", "desc");
        Article article2 = createArticle("title2", "random2", "desc3");
        article.setUser(user);
        article2.setUser(user);
        Article savedArticle = articleRepository.save(article);
        articleRepository.save(article2);
        ArticleRequest articleRequest = new ArticleRequest.Builder().withBody(
                "body").withTitle("title").withDescription("desc").build();
        String json = objectMapper.writeValueAsString(articleRequest);
        this.mockMvc.perform(
                patch("/api/articles/{slug_id}", slugIdGenerator.apply(savedArticle))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(httpBasic("user", "1234")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shoulde_not_delete_an_article_if_role_is_not_admin() throws Exception {

        Article article = new Article.Builder().withBody("abc").withDescription(
                "efef").withTitle("fefe").build();
        User user1 = new User.Builder().withUserName("user1").withEmail(
                "user1@gmail.com").withPassword(passwordEncoder.encode("1234")).withRole(
                WriterPadRole.EDITOR).build();
        User editor = userRepository.save(user1);
        article.setUser(editor);
        Article saved = articleRepository.save(article);
        this.mockMvc.perform(
                delete("/api/articles/{id}", slugIdGenerator.apply(saved))
                        .contentType(MediaType.APPLICATION_JSON).with(
                        httpBasic("user1", "1234")))
                .andDo(print())
                .andExpect(status().isForbidden());
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