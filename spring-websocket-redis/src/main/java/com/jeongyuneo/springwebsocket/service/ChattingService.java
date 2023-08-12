package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomCreateRequest;
import com.jeongyuneo.springwebsocket.dto.MessageRequest;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChattingService {

    private final RedisPublisher redisPublisher;
    private final ChattingRoomRepository chattingRoomRepository;

    public void send(MessageRequest messageRequest) {
        redisPublisher.publish(chattingRoomRepository.getTopic(messageRequest.getChattingRoomId()), messageRequest);
        chattingRoomRepository.saveChatting(messageRequest.getChattingRoomId(), messageRequest.toChatting());
    }

    public void createChattingRoom(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        chattingRoomRepository.createChattingRoom(chattingRoomCreateRequest.getName());
    }

    public void enterChattingRoom(String chattingRoomId) {
        chattingRoomRepository.enterChattingRoom(chattingRoomId);
    }

    public List<ChattingRoom> getChattingRooms() {
        return chattingRoomRepository.findAllRoom();
    }

    public ChattingRoom getChattingRoom(String chattingRoomId) {
        return chattingRoomRepository.findRoomById(chattingRoomId);
    }

    public List<MessageRequest> getChattings(String chattingRoomId) {
        log.info("find chattings in chatting room: {}", chattingRoomId);
        return chattingRoomRepository.findChattingByChattingRoomId(chattingRoomId)
                .stream()
                .map(chatting -> MessageRequest.builder()
                        .chattingRoomId(chatting.getId())
                        .senderId(chatting.getSenderId())
                        .content(chatting.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
