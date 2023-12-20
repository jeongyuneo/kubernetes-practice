package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.dto.MemberSearchCondition;
import com.jeongyuneo.querydsl.dto.MemberTeamDto;
import com.jeongyuneo.querydsl.dto.QMemberTeamDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static com.jeongyuneo.querydsl.entity.QTeam.team;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    @Override
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

    private BooleanExpression ageBetween(Integer ageLoe, Integer ageGoe) {
        if (ageLoe == null || ageGoe == null) {
            return null;
        }
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
