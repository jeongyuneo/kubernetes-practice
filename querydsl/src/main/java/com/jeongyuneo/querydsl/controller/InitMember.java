package com.jeongyuneo.querydsl.controller;

import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
@Profile("local")
@Component
public class InitMember {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {

        @PersistenceContext
        private EntityManager entityManager;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            entityManager.persist(teamA);
            entityManager.persist(teamB);

            for (int i = 1; i <= 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                entityManager.persist(new Member("member" + i, i, selectedTeam));
            }
        }
    }
}
