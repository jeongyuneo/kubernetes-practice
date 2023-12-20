package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface CustomMemberRepository {

    List<MemberTeamDto> search(MemberSearchCondition condition);
}
