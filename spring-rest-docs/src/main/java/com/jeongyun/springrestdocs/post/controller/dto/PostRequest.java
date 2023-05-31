package com.jeongyun.springrestdocs.post.controller.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {

    private String title;
    private String content;
    private String author;
}
