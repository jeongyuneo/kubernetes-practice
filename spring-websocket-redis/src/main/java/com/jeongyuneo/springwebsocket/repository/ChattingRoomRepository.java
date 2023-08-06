package com.jeongyuneo.springwebsocket.repository;

import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.service.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChattingRoomRepository {

    private static final String CHATTING_ROOM = "CHATTING_ROOM";
    private static final Map<String, ChannelTopic> TOPICS = new HashMap<>();

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisSubscriber redisSubscriber;

    private HashOperations<String, String, ChattingRoom> opsHashChattingRoom;

    @PostConstruct
    private void init() {
        opsHashChattingRoom = redisTemplate.opsForHash();
    }

    public ChannelTopic getTopic(String chattingRoomId) {
        return TOPICS.get(chattingRoomId);
    }

    public void createChattingRoom(String name) {
        ChattingRoom chattingRoom = ChattingRoom.from(name);
        opsHashChattingRoom.put(CHATTING_ROOM, chattingRoom.getId().toString(), chattingRoom);
        log.info("create new chatting room '{}'", chattingRoom.getId());
    }

    public void enterChattingRoom(String chattingRoomId) {
        ChannelTopic topic = getTopic(chattingRoomId);
        if (topic == null) {
            topic = new ChannelTopic(String.valueOf(chattingRoomId));
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            TOPICS.put(chattingRoomId, topic);
        }
        log.info("enter in chatting room '{}'", chattingRoomId);
    }

    public List<ChattingRoom> findAllRoom() {
        return opsHashChattingRoom.values(CHATTING_ROOM);
    }

    public ChattingRoom findRoomById(String chattingRoomId) {
        return opsHashChattingRoom.get(CHATTING_ROOM, chattingRoomId);
    }
}
