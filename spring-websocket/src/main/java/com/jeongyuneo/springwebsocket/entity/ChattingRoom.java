package com.jeongyuneo.springwebsocket.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chatting_room SET deleted = true WHERE id=?")
@AttributeOverride(name = "id", column = @Column(name = "chatting_room_id"))
@Entity
public class ChattingRoom extends BaseEntity {

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private final List<Chatting> chattings = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public void createChatting(Chatting chatting) {
        chattings.add(chatting);
        Member sender = chatting.getMember();
        MemberChattingRoom memberChattingRoom = MemberChattingRoom.builder()
                .member(sender)
                .chattingRoom(this)
                .build();
        memberChattingRooms.add(memberChattingRoom);
        sender.createChatting(memberChattingRoom);
    }
}
