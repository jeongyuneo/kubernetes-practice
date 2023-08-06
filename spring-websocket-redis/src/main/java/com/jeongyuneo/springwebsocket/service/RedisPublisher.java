package com.jeongyuneo.springwebsocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongyuneo.springwebsocket.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(ChannelTopic topic, MessageRequest messageRequest) {
        redisTemplate.convertAndSend(topic.getTopic(), toJson(messageRequest));
    }

    private String toJson(MessageRequest messageRequest) {
        try {
            return objectMapper.writeValueAsString(messageRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
