package com.jeongyun.springrestdocs.post.controller;

import com.jeongyun.springrestdocs.ApiDocument;
import com.jeongyun.springrestdocs.post.controller.dto.PostRequest;
import com.jeongyun.springrestdocs.post.controller.dto.PostResponse;
import com.jeongyun.springrestdocs.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest extends ApiDocument {

    private static final String SUCCESS_MESSAGE = "success";

    private PostRequest postRequest;
    private PostResponse successResponse;

    // (1) mocking을 하기 위해 @MockBean 선언
    @MockBean
    private PostService postService;

    // (2) 반복적으로 사용되는 값들을 전역으로 선언 후 초기화
    @BeforeEach
    void setUp() {
        postRequest = PostRequest.builder()
                .title("Spring REST Docs 연습")
                .content("즐거운 Spring REST Docs 연습 시간")
                .author("jeongyuneo")
                .build();
        successResponse = PostResponse.builder()
                .message(SUCCESS_MESSAGE)
                .build();
    }

    @DisplayName("게시글 저장 성공")
    @Test
    void create_post_success() throws Exception {
        // given
        willReturn(successResponse).given(postService).create(any(PostRequest.class));  // (3) mocking을 하여 예상 응답값 받음
        // when
        ResultActions resultActions = 게시글_저장_요청(postRequest);
        // then
        게시글_저장_성공(resultActions);
    }

    private ResultActions 게시글_저장_요청(PostRequest postRequest) throws Exception {
        return mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(postRequest))); // (4) ApiDocument에 정의한 toJson() 메소드를 사용해 JSON 파싱
    }

    private void 게시글_저장_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(toJson(successResponse)))
                .andDo(print())
                .andDo(toDocument("create-post-success"));  // (5) ApiDocument에 정의한 toDocument() 메소드를 사용해 문서 작성
    }
}
