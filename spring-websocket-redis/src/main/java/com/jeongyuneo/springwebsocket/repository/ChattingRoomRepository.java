package com.jeongyuneo.springwebsocket.repository;

import com.jeongyuneo.springwebsocket.entity.Chatting;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChattingRoomRepository {

    private static final String CHATTING_ROOM = "CHATTING_ROOM";
    private static final String DELIMITER = ":";

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ChattingRoom> opsHashChattingRoom;
    private HashOperations<String, String, Chatting> opsHashChatting;

    @PostConstruct
    private void init() {
        opsHashChattingRoom = redisTemplate.opsForHash();
        opsHashChatting = redisTemplate.opsForHash();
    }

    public void createChattingRoom(String name) {
        ChattingRoom chattingRoom = ChattingRoom.from(name);
        opsHashChattingRoom.put(CHATTING_ROOM, chattingRoom.getId().toString(), chattingRoom);
        log.info("create new chatting room '{}'", chattingRoom.getId());
    }

    public List<ChattingRoom> findAllRoom() {
        return opsHashChattingRoom.values(CHATTING_ROOM);
    }

    public ChattingRoom findById(String chattingRoomId) {
        return opsHashChattingRoom.get(CHATTING_ROOM, chattingRoomId);
    }

    public void saveChatting(String chattingRoomId, Chatting chatting) {
        opsHashChatting.put(CHATTING_ROOM + DELIMITER + chattingRoomId, chatting.getId(), chatting);
    }

    public List<Chatting> findChattingByChattingRoomId(String chattingRoomId) {
        log.info("get chattings in {}", chattingRoomId);
        return opsHashChatting.values(CHATTING_ROOM + DELIMITER + chattingRoomId);
    }
}
