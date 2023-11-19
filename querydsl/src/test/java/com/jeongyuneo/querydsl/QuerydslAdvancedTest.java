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
}
