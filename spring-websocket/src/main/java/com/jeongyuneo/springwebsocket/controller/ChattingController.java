package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.ChattingRequest;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChattingController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chattings/{chattingRoomId}/messages")
    public void chat(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingId);
        simpMessagingTemplate.convertAndSend("/subscription/chattings/" + chattingId, chattingRequest.getContent());
    }
}
