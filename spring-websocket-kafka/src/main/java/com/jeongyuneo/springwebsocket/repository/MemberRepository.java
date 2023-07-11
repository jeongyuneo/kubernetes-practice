package com.jeongyuneo.springwebsocket.repository;

import com.jeongyuneo.springwebsocket.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
