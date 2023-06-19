package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.ChattingRequest;
import com.jeongyuneo.springwebsocket.entity.Chatting;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.entity.Member;
import com.jeongyuneo.springwebsocket.repository.ChattingRoomRepository;
import com.jeongyuneo.springwebsocket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Transactional
    public void save(Long chattingId, ChattingRequest chattingRequest) {
        Member sender = memberRepository.findById(chattingRequest.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("There's no member"));
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingId)
                .orElseThrow(() -> new IllegalArgumentException("There's no chatting room"));
        Chatting chatting = Chatting.builder()
                .member(sender)
                .chattingRoom(chattingRoom)
                .content(chattingRequest.getContent())
                .build();
        chattingRoom.createChatting(chatting);
        chattingRoomRepository.save(chattingRoom);
    }
}
