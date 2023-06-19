package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.ChattingRequest;
import com.jeongyuneo.springwebsocket.dto.ChattingRoomResponse;
import com.jeongyuneo.springwebsocket.entity.Chatting;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.entity.Member;
import com.jeongyuneo.springwebsocket.repository.ChattingRoomRepository;
import com.jeongyuneo.springwebsocket.repository.MemberChattingRoomRepository;
import com.jeongyuneo.springwebsocket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChattingService {

    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final MemberChattingRoomRepository memberChattingRoomRepository;

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

    @Transactional(readOnly = true)
    public List<ChattingRoomResponse> getAll(Long memberId) {
        return memberChattingRoomRepository.findByMemberId(memberId)
                .stream()
                .map(chattingRoomMember -> {
                    ChattingRoom chattingRoom = chattingRoomMember.getChattingRoom();
                    return ChattingRoomResponse.builder()
                            .chattingRoomId(chattingRoom.getId())
                            .name(chattingRoom.getName())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
