package com.jeongyuneo.querydsl.controller;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;
import com.jeongyuneo.querydsl.repository.MemberJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberJPARepository memberJPARepository;

    @GetMapping("/v1")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJPARepository.search(condition);
    }
}
