package com.example.restea.share.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.restea.ResteaApplication;
import com.example.restea.oauth2.dto.CustomOAuth2User;
import com.example.restea.oauth2.service.CustomOAuth2UserService;
import com.example.restea.share.dto.ShareCommentCreationRequest;
import com.example.restea.share.entity.ShareBoard;
import com.example.restea.share.entity.ShareComment;
import com.example.restea.share.repository.ShareBoardRepository;
import com.example.restea.share.repository.ShareCommentRepository;
import com.example.restea.user.entity.User;
import com.example.restea.user.repository.UserRepository;
import com.example.restea.util.SecurityTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration(classes = ResteaApplication.class)
@AutoConfigureMockMvc
public class CreateShareCommentTest {
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;
    private final WebApplicationContext context;
    private final ShareBoardRepository shareBoardRepository;
    private final ShareCommentRepository shareCommentRepository;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService custumOAuth2UserService;

    private CustomOAuth2User customOAuth2User;

    @Autowired
    public CreateShareCommentTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                  WebApplicationContext context,
                                  ShareBoardRepository shareBoardRepository,
                                  ShareCommentRepository shareCommentRepository, UserRepository userRepository
            , CustomOAuth2UserService custumOAuth2UserService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.context = context;
        this.shareBoardRepository = shareBoardRepository;
        this.shareCommentRepository = shareCommentRepository;
        this.userRepository = userRepository;
        this.custumOAuth2UserService = custumOAuth2UserService;
    }

    // nickname : "TestUser", authId : "authId", authToken : "authToken"
    @Transactional
    @BeforeEach
    public void mockMvcSetUp() {
        userRepository.deleteAll();
        shareBoardRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // mockMvc에서 @AuthenticationPrincipal CustomOAuth2User를 사용하기 위해
    @BeforeEach
    public void OAuth2UserSetup() {
        customOAuth2User = custumOAuth2UserService.handleNewUser("authId", "authToken");
        SecurityTestUtil.setUpSecurityContext(customOAuth2User);
    }

    @AfterEach
    public void tearDown() {
        shareCommentRepository.deleteAll();
        shareBoardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("createShareComment : 나눔 게시글 댓글 생성에 성공한다.")
    @Test
    public void createShareComment_Success() throws Exception {

        // given
        User user = userRepository.findByAuthId("authId")
                .orElseThrow(() -> new RuntimeException("테스트를 위한 유저 생성 실패"));

        final String title = "Title";
        final String shareBoardcontent = "Content";
        final Integer maxParticipants = 10;
        final LocalDateTime endDate = LocalDateTime.now().plusWeeks(1L);
        ShareBoard shareBoard = shareBoardRepository.save(ShareBoard.builder()
                .title(title)
                .content(shareBoardcontent)
                .maxParticipants(maxParticipants)
                .endDate(endDate)
                .user(user)
                .build());

        final String url = "/api/v1/shares/" + shareBoard.getId() + "/comments";
        final String content = "TestContent";
        final ShareCommentCreationRequest shareCommentCreationRequest = new ShareCommentCreationRequest(content);
        final String requestBody = objectMapper.writeValueAsString(shareCommentCreationRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());
        List<ShareComment> shareComments = shareCommentRepository.findAll();
        assertThat(shareComments.size()).isEqualTo(1);
        assertThat(shareComments.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("createShareComment : 입력값이 null인 경우.")
    @Test
    public void createShareComment_NullContent_Failure() throws Exception {
        // given
        User user = userRepository.findByAuthId("authId")
                .orElseThrow(() -> new RuntimeException("테스트를 위한 유저 생성 실패"));

        final String title = "Title";
        final String shareBoardcontent = "Content";
        final Integer maxParticipants = 10;
        final LocalDateTime endDate = LocalDateTime.now().plusWeeks(1L);
        ShareBoard shareBoard = shareBoardRepository.save(ShareBoard.builder()
                .title(title)
                .content(shareBoardcontent)
                .maxParticipants(maxParticipants)
                .endDate(endDate)
                .user(user)
                .build());

        final String url = "/api/v1/shares/" + shareBoard.getId() + "/comments";
        final ShareCommentCreationRequest shareCommentCreationRequest = new ShareCommentCreationRequest(null);
        final String requestBody = objectMapper.writeValueAsString(shareCommentCreationRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        List<ShareComment> shareComments = shareCommentRepository.findAll();
        assertThat(shareComments.size()).isEqualTo(0);
    }

    @DisplayName("createShareBoard : content가 비었을 때 실패한다.")
    @Test
    public void createShareBoard_EmptyContent_Failure() throws Exception {
        // given
        User user = userRepository.findByAuthId("authId")
                .orElseThrow(() -> new RuntimeException("테스트를 위한 유저 생성 실패"));

        final String title = "Title";
        final String shareBoardcontent = "Content";
        final Integer maxParticipants = 10;
        final LocalDateTime endDate = LocalDateTime.now().plusWeeks(1L);
        ShareBoard shareBoard = shareBoardRepository.save(ShareBoard.builder()
                .title(title)
                .content(shareBoardcontent)
                .maxParticipants(maxParticipants)
                .endDate(endDate)
                .user(user)
                .build());

        final String url = "/api/v1/shares/" + shareBoard.getId() + "/comments";
        final String content = "";
        final ShareCommentCreationRequest shareCommentCreationRequest = new ShareCommentCreationRequest(content);
        final String requestBody = objectMapper.writeValueAsString(shareCommentCreationRequest);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        List<ShareComment> shareComments = shareCommentRepository.findAll();
        assertThat(shareComments.size()).isEqualTo(0);
    }
    
}
