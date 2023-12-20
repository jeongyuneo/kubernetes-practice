package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
