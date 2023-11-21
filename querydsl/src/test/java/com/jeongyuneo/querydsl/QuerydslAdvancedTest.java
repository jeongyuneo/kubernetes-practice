package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.dto.MemberDto;
import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongyuneo.querydsl.entity.QMember.member;

@Transactional
@SpringBootTest
public class QuerydslAdvancedTest {

    @Autowired
    EntityManager entityManager;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(entityManager);

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
    }

    @Test
    void Querydsl을_이용해_회원이름을_조회한다() {
        // when
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
        // then
        for (String member : result) {
            System.out.println(member);
        }
    }

    @Test
    void Querydsl을_이용해_회원이름과_나이를_조회한다() {
        // when
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();
        // then
        for (Tuple tuple : result) {
            System.out.println(tuple.get(member.username) + ", " + tuple.get(member.age));
        }
    }

    @Test
    void JPQL을_이용해_DTO로_회원이름과_나이를_조회한다() {
        // when
        List<MemberDto> result = entityManager.createQuery("select new com.jeongyuneo.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();
        // then
        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다1() {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        // then
        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다2() {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        // then
        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다3() {
        // when
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        // then
        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }
}
