package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.Message;
import com.jeongyuneo.springwebsocket.util.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final JsonParser jsonParser;

    public void publish(ChannelTopic topic, Message message) {
        redisTemplate.convertAndSend(topic.getTopic(), jsonParser.toJson(message));
    }
}
