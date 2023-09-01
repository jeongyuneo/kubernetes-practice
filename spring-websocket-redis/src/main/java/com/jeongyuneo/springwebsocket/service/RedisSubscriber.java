package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.Message;
import com.jeongyuneo.springwebsocket.util.JsonParser;
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

    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate redisTemplate;
    private final JsonParser jsonParser;

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        Message messageRequest = jsonParser.toMessageRequest((String) redisTemplate.getStringSerializer().deserialize(message.getBody()));
        messagingTemplate.convertAndSend("/subscription/chattings/rooms/" + messageRequest.getChattingRoomId(), messageRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", messageRequest.getContent(), messageRequest.getSenderId(), messageRequest.getChattingRoomId());
    }
}
