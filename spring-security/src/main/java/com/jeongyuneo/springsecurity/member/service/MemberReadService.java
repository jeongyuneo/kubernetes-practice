package com.jeongyuneo.springsecurity.member.service;

import com.jeongyuneo.springsecurity.exception.ApplicationExceptionInfo;
import com.jeongyuneo.springsecurity.exception.NotFoundException;
import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ApplicationExceptionInfo.MEMBER_NOT_FOUND));
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApplicationExceptionInfo.MEMBER_NOT_FOUND));
    }
}
