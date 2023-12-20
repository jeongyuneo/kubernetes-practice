package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void Spring_Data_JPA를_이용한_메소드들이_정상적으로_동작한다() {
        // given
        Member member = new Member("member1", 10);
        // when
        memberRepository.save(member);
        // then
        assertAll(
                () -> assertThat(memberRepository.findById(member.getId()).orElseThrow()).isEqualTo(member),
                () -> assertThat(memberRepository.findAll()).containsExactly(member),
                () -> assertThat(memberRepository.findByUsername(member.getUsername())).containsExactly(member)
        );
    }
}
