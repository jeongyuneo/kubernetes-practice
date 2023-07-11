package com.jeongyuneo.springwebsocket.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chatting SET deleted = true WHERE id=?")
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@Entity
public class Member extends BaseEntity {

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private final List<MemberChattingRoom> memberChattingRooms = new ArrayList<>();

    public void createChatting(MemberChattingRoom memberChattingRoom) {
        memberChattingRooms.add(memberChattingRoom);
    }
}
