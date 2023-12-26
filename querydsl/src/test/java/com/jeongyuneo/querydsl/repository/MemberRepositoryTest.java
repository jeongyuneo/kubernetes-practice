package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;
import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    EntityManager entityManager;

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
        List<MemberTeamDto> result = memberRepository.search(condition);
        // then
        assertThat(result).extracting("username").containsExactly("member4");
    }

    @Test
    void Querydsl을_이용해_사이즈3인_페이지의_0번째_회원페이지를_검색한다() {
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

        MemberSearchCondition condition = new MemberSearchCondition(null, null, null, null);
        PageRequest pageRequest = PageRequest.of(0, 3);
        // when
        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);
        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3")
        );
    }
}
