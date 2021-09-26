package com.example.service;

import static com.example.service.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.configuration.DaoFactory;
import com.example.dao.UserDao;
import com.example.domain.Level;
import com.example.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserServiceTest {

    private UserService userService;
    private UserDao userDao;
    List<User> users;

    @BeforeEach
    void setUp() {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        this.userService = context.getBean("userService", UserService.class);

        users = Arrays.asList(
            new User("wade", "yang", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0),
            new User("joe", "kim", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
            new User("thomas", "lee", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("henry", "jang", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("ron", "oh", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
        userDao = userService.userDao;
        userDao.deleteAll();
    }

    @Test
    void upgradeLevels() {

        users.forEach(user -> userDao.add(user));

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }

}
