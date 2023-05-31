package com.jeongyun.springrestdocs.post.controller;

import com.jeongyun.springrestdocs.post.controller.dto.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PostResponse> runtimeException() {
        return ResponseEntity.badRequest().body(PostResponse.builder().message("fail").build());
    }
}
