package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.dto.MemberDto;
import com.jeongyuneo.querydsl.dto.QMemberDto;
import com.jeongyuneo.querydsl.dto.UserDto;
import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.QMember;
import com.jeongyuneo.querydsl.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다4() {
        // when
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();
        // then
        for (UserDto userDto : result) {
            System.out.println(userDto);
        }
    }

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다5() {
        // given
        QMember subMember = new QMember("subMember");
        // when
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember), "age")))
                .from(member)
                .fetch();
        // then
        for (UserDto userDto : result) {
            System.out.println(userDto);
        }
    }

    @Test
    void Querydsl을_이용해_DTO로_회원이름과_나이를_조회한다6() {
        // when
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
        // then
        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    @Test
    void Querydsl을_이용해_회원이름과_나이로_회원을_검색한다1() {
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;
        // when
        List<Member> result = searchMember1(usernameParam, ageParam);
        // then
        assertThat(result).hasSize(1);
    }

    private List<Member> searchMember1(String usernameCondition, Integer ageCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (usernameCondition != null) {
            booleanBuilder.and(member.username.eq(usernameCondition));
        }
        if (ageCondition != null) {
            booleanBuilder.and(member.age.eq(ageCondition));
        }
        return queryFactory
                .selectFrom(member)
                .where(booleanBuilder)
                .fetch();
    }

    @Test
    void Querydsl을_이용해_회원이름과_나이로_회원을_검색한다2() {
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;
        // when
        List<Member> result = searchMember2(usernameParam, ageParam);
        // then
        assertThat(result).hasSize(1);
    }

    private List<Member> searchMember2(String usernameCondition, Integer ageCondition) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCondition), ageEq(ageCondition))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCondition) {
        if (usernameCondition == null) {
            return null;
        }
        return member.username.eq(usernameCondition);
    }

    private BooleanExpression ageEq(Integer ageCondition) {
        if (ageCondition == null) {
            return null;
        }
        return member.age.eq(ageCondition);
    }

    @Test
    void Querydsl을_이용해_회원이름과_나이로_회원을_검색한다3() {
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;
        // when
        List<Member> result = searchMember3(usernameParam, ageParam);
        // then
        assertThat(result).hasSize(1);
    }

    private List<Member> searchMember3(String usernameCondition, Integer ageCondition) {
        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameCondition, ageCondition))
                .fetch();
    }

    private BooleanExpression allEq(String usernameCondition, Integer ageCondition) {
        return usernameEq(usernameCondition).and(ageEq(ageCondition));
    }

    @Test
    void Querydsl을_이용해_나이가_28살보다_어린_회원의_이름을_수정한다() {
        // given
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
        entityManager.flush();
        entityManager.clear();
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();
        // then
        assertThat(count).isEqualTo(2);
        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    void Querydsl을_이용해_나이가_18살_이상인_회원을_모두_삭제한다() {
        // given
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
        entityManager.flush();
        entityManager.clear();
        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();
        // then
        assertThat(count).isEqualTo(3);
        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    void Querydsl을_이용해_모든_회원이름을_member를_M으로_대체해서_조회한다() {
        // when
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();
        // then
        for (String username : result) {
            System.out.println(username);
        }
    }
}
