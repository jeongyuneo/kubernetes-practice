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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChattingService {

    private final RedisPublisher redisPublisher;
    private final ChattingRoomRepository chattingRoomRepository;

    public void send(MessageRequest messageRequest) {
        redisPublisher.publish(chattingRoomRepository.getTopic(messageRequest.getChattingRoomId()), messageRequest);
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
}
