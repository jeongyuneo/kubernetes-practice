package com.jeongyuneo.springwebsocket.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRequest {

    private String chattingRoomId;
    private Long senderId;
    private String content;
}
