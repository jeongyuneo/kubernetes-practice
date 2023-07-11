package com.jeongyuneo.springwebsocket.repository;

import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
