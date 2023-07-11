package com.jeongyuneo.springwebsocket.repository;

import com.jeongyuneo.springwebsocket.entity.MemberChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChattingRoomRepository extends JpaRepository<MemberChattingRoom, Long> {

    List<MemberChattingRoom> findByMemberId(Long memberId);
}
