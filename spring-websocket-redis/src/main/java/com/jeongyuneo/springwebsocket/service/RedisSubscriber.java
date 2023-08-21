package com.jeongyuneo.springwebsocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongyuneo.springwebsocket.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        Message messageRequest = toMessageRequest((String) redisTemplate.getStringSerializer().deserialize(message.getBody()));
        messagingTemplate.convertAndSend("/subscription/chattings/rooms/" + messageRequest.getChattingRoomId(), messageRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", messageRequest.getContent(), messageRequest.getSenderId(), messageRequest.getChattingRoomId());
    }

    private Message toMessageRequest(String chattingMessage) {
        try {
            return objectMapper.readValue(chattingMessage, Message.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
