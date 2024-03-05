package com.jeongyuneo.springsecurity.member.service;

import com.jeongyuneo.springsecurity.member.dto.SignupRequest;
import com.jeongyuneo.springsecurity.member.dto.SignupResponse;
import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberWriteService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public SignupResponse signup(SignupRequest signupRequest) {
        Member member = Member.of(signupRequest, passwordEncoder);
        memberRepository.save(member);
        return SignupResponse.from(member.getId());
    }
}
