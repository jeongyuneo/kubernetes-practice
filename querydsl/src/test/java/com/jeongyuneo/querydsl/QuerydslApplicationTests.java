package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.entity.Hello;
import com.jeongyuneo.querydsl.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class QuerydslApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
        // given
        Hello hello = new Hello();
        entityManager.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QHello qHello = QHello.hello;
        // when
        Hello result = query
                .selectFrom(qHello)
                .fetchOne();
        // then
        assertThat(result).isEqualTo(hello);
    }
}
