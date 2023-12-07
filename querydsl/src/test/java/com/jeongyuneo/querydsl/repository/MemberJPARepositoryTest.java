package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;
import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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

    @Test
    void Querydsl을_이용해_35살이상_40살이하이고_팀B에_속한_회원을_검색한다() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        MemberSearchCondition condition = new MemberSearchCondition(null, "teamB", 35, 40);
        // when
        List<MemberTeamDto> result = memberJPARepository.searchByBuilder(condition);
        // then
        assertThat(result).extracting("username").containsExactly("member4");
    }
}
