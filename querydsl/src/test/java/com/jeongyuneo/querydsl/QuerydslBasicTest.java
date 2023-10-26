package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class QuerydslBasicTest {

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
    void JPQL을_이용해_멤버1을_찾는다() {
        // given
        String query = "select m from Member m where m.username = :username";
        // when
        Member findMember = entityManager.createQuery(query, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void Querydsl을_이용해_멤버1을_찾는다() {
        // given
        // when
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void Querydsl을_이용해_나이가_10인_멤버1을_찾는다() {
        // given
        // when
        // and 연산자
        Member findMember1 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1")
                                .and(member.age.eq(10)))
                .fetchOne();
        // 파라미터 나열
        Member findMember2 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10))
                .fetchOne();
        // then
        assertThat(findMember1.getUsername()).isEqualTo("member1");
        assertThat(findMember1.getAge()).isEqualTo(10);

        assertThat(findMember2.getUsername()).isEqualTo("member1");
        assertThat(findMember2.getAge()).isEqualTo(10);
    }
}
