package com.example.dao;

import com.example.configuration.DaoFactory;
import com.example.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    private UserDao dao;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        this.dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        this.user1 = new User("wade", "yang", "abcd1234");
        this.user2 = new User("kihyun", "yang", "abcd1234");
    }

    @Test
    void addAndGet() throws SQLException {

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userget1 = dao.get(user1.getId());

        assertThat(userget1.getName()).isEqualTo("yang");
        assertThat(userget1.getPassword()).isEqualTo("abcd1234");
    }

}