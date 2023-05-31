package com.jeongyun.springrestdocs.post.controller;

import com.jeongyun.springrestdocs.post.controller.dto.PostRequest;
import com.jeongyun.springrestdocs.post.controller.dto.PostResponse;
import com.jeongyun.springrestdocs.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.create(postRequest));
    }
}
