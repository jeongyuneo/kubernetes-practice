package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MemberJPARepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberJPARepository memberJPARepository;

    @Test
    void JPQL을_이용해_만든_레포지토리의_메소드들이_정상적으로_동작한다() {
        // given
        Member member = new Member("member1", 10);
        // when
        memberJPARepository.save(member);
        // then
        assertAll(
                () -> assertThat(memberJPARepository.findById(member.getId()).orElseThrow()).isEqualTo(member),
                () -> assertThat(memberJPARepository.findAll()).containsExactly(member),
                () -> assertThat(memberJPARepository.findByUsername("member1")).containsExactly(member)
        );
    }

    @Test
    void Querydsl을_이용해_만든_레포지토리의_메소드들이_정상적으로_동작한다() {
        // given
        Member member = new Member("member1", 10);
        // when
        memberJPARepository.save(member);
        // then
        assertAll(
                () -> assertThat(memberJPARepository.findAll_Querydsl()).containsExactly(member),
                () -> assertThat(memberJPARepository.findByUsername_Querydsl("member1")).containsExactly(member)
        );
    }
}
