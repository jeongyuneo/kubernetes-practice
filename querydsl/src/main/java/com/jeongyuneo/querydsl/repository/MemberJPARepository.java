package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;
import com.jeongyuneo.querydsl.dto.QMemberTeamDto;
import com.jeongyuneo.querydsl.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static com.jeongyuneo.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class MemberJPARepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return entityManager.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findAll_Querydsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeGoe())
                )
                .fetch();
    }

    public List<Member> searchMember(MemberSearchCondition condition) {
        return queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageBetween(condition.getAgeLoe(), condition.getAgeGoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCondition) {
        if (usernameCondition == null) {
            return null;
        }
        return member.username.eq(usernameCondition);
    }

    private BooleanExpression teamNameEq(String teamName) {
        if (teamName == null) {
            return null;
        }
        return team.name.eq(teamName);
    }

    private BooleanExpression ageBetween(int ageLoe, int ageGoe) {
        return ageLoe(ageLoe).and(ageGoe(ageGoe));
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        if (ageGoe == null) {
            return null;
        }
        return member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        if (ageLoe == null) {
            return null;
        }
        return member.age.loe(ageLoe);
    }
}
