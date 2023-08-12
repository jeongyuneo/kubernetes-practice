package com.jeongyuneo.springwebsocket.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatting implements Serializable {

    @Id
    private final String id;

    private final Long senderId;
    private final String content;

    public static Chatting of(Long senderId, String content) {
        return new Chatting(UUID.randomUUID().toString(), senderId, content);
    }
}
