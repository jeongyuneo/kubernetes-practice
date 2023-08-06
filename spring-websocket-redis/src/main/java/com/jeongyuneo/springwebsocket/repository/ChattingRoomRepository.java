package com.jeongyuneo.springwebsocket.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChattingRoomRepository {

    private static final Map<String, ChannelTopic> TOPICS = new HashMap<>();

    public ChannelTopic getTopic(String chattingRoomId) {
        return TOPICS.get(chattingRoomId);
    }
}
