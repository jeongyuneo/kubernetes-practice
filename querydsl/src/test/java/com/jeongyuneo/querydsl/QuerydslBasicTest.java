package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.QMember;
import com.jeongyuneo.querydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static com.jeongyuneo.querydsl.entity.QTeam.team;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class QuerydslBasicTest {

    @Autowired
    EntityManager entityManager;

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

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

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 오름차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    void Querydsl을_이용해_정렬된_회원을_조회한다() {
        // given
        entityManager.persist(new Member(null, 100));
        entityManager.persist(new Member("member5", 100));
        entityManager.persist(new Member("member6", 100));
        // when
        List<Member> findMembers = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();
        // then
        assertThat(findMembers.get(0).getUsername()).isEqualTo("member5");
        assertThat(findMembers.get(1).getUsername()).isEqualTo("member6");
        assertThat(findMembers.get(2).getUsername()).isNull();
    }

    @Test
    void Querydsl을_이용해_조회건수를_제한한다() {
        // when
        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
        // then
        assertThat(members).hasSize(2);
    }

    @Test
    void Querydsl을_이용해_전체_조회수를_조회한다() {
        // when
        QueryResults<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();
        // then
        assertThat(members.getTotal()).isEqualTo(4);
        assertThat(members.getLimit()).isEqualTo(2);
        assertThat(members.getOffset()).isEqualTo(1);
        assertThat(members.getResults()).hasSize(2);
    }

    @Test
    void Querydsl을_이용해_팀이름과_각팀의_평균연령을_조회한다() {
        // when
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();
        // then
        assertThat(result.get(0).get(team.name)).isEqualTo("teamA");
        assertThat(result.get(0).get(member.age.avg())).isEqualTo(15); // (10 + 20) / 2
        assertThat(result.get(1).get(team.name)).isEqualTo("teamB");
        assertThat(result.get(1).get(member.age.avg())).isEqualTo(35); // (30 + 40) / 2
    }

    @Test
    void Querydsl을_이용해_평균연령이_20이상인_팀이름과_평균연령을_조회한다() {
        // when
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .having(member.age.avg().goe(20))
                .fetch();
        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).get(team.name)).isEqualTo("teamB");
        assertThat(result.get(0).get(member.age.avg())).isEqualTo(35); // (30 + 40) / 2
    }

    @Test
    void Querydsl을_이용해_팀A에_속한_모든_회원을_조회한다() {
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
        // then
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    @Test
    void Querydsl을_이용해_팀이름과_회원이름이_같은_회원을_조회한다() {
        // given
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        // when
        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();
        // then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    @Test
    void Querydsl을_이용해_팀A만_조인해서_모든_회원을_조회한다() {
        // JPQL: select m, t from Member m left join m.team t on t.name = 'teamA'
        // when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
        // then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).get(team).getName()).isEqualTo("teamA");
        assertThat(result.get(1).get(team).getName()).isEqualTo("teamA");
        assertThat(result.get(2).get(team)).isNull();
        assertThat(result.get(3).get(team)).isNull();
    }

    /**
     * 연관관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    void Querydsl을_이용해_팀이름과_회원이름이_같은_회원을_조회한다2() {
        // given
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        entityManager.persist(new Member("teamC"));
        // when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();
        Tuple teamA = result.get(4);
        Tuple teamB = result.get(5);
        // then
        assertThat(result).hasSize(7);
        assertThat(teamA.get(member).getUsername()).isEqualTo(teamA.get(team).getName());
        assertThat(teamB.get(member).getUsername()).isEqualTo(teamB.get(team).getName());
    }

    @Test
    void Querydsl을_이용해_페치조인없이_팀을_조회하지_못한다() {
        // given
        entityManager.flush();
        entityManager.clear();
        // when
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        // then
        assertThat(entityManagerFactory.getPersistenceUnitUtil().isLoaded(findMember.getTeam())).isFalse();
    }

    @Test
    void Querydsl을_이용해_페치조인으로_팀을_조회한다() {
        // given
        entityManager.flush();
        entityManager.clear();
        // when
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
        // then
        assertThat(entityManagerFactory.getPersistenceUnitUtil().isLoaded(findMember.getTeam())).isTrue();
    }

    @Test
    void Querydsl을_이용해_서브쿼리로_나이가_가장_많은_회원을_조회한다() {
        // given
        QMember subMember = new QMember("subMember");
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(subMember.age.max())
                                .from(subMember)
                ))
                .fetch();
        // then
        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    void Querydsl을_이용해_서브쿼리로_나이가_평귱_이상인_회원을_조회한다() {
        // given
        QMember subMember = new QMember("subMember");
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(subMember.age.avg())
                                .from(subMember)
                ))
                .fetch();
        // then
        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    void Querydsl을_이용해_서브쿼리로_회원들의_평균_나이를_조회한다() {
        // given
        QMember subMember = new QMember("subMember");
        // when
        List<Tuple> result = queryFactory
                .select(member.username,
                        select(subMember.age.avg())
                                .from(subMember))
                .from(member)
                .fetch();
        // then
        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }

    @Test
    void QueryDsl을_이용해_회원_나이를_케이스별로_출력한다() {
        // when
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        // then
        for (String age : result) {
            System.out.println("나이: " + age);
        }
    }

    @Test
    void QueryDsl을_이용해_회원_나이를_케이스별로_출력한다2() {
        // when
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        // then
        for (String age : result) {
            System.out.println("나이: " + age);
        }
    }

    @Test
    void QueryDsl을_이용해_회원이름과_상수를_함께_출력한다() {
        // when
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();
        // then
        for (Tuple tuple : result) {
            System.out.println("나이: " + tuple);
        }
    }

    @Test
    void QueryDsl을_이용해_회원이름과_나이를_더해서_조회한다() {
        // when
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();
        // then
        for (String memberInfo : result) {
            System.out.println(memberInfo);
        }
    }

    @Test
    void QueryDsl을_이용해_중복없는_회원이름을_조회한다() {
        // when
        List<String> result = queryFactory
                .select(member.username)
                .distinct()
                .from(member)
                .fetch();
        // then
        for (String memberInfo : result) {
            System.out.println(memberInfo);
        }
    }
}
