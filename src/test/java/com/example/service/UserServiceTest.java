package com.example.service;

import static com.example.service.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.configuration.DaoFactory;
import com.example.dao.UserDao;
import com.example.domain.Level;
import com.example.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserServiceTest {

    private UserService userService;
    private UserDao userDao;
    private DataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp() {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        this.userService = context.getBean("userService", UserService.class);

        users = new ArrayList<>();
        users.add(new User("wade", "yang", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0));
        users.add(new User("joe", "kim", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0));
        users.add(new User("thomas", "lee", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1));
        users.add(new User("henry", "jang", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD));
        users.add(new User("ron", "oh", "p5", Level.GOLD, 100, Integer.MAX_VALUE));

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

    @Test
    void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setDataSource(userService.dataSource);

        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
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

    static class TestUserService extends UserService {

        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException("테스트 유저 서비스 예외 발생!");
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

        public TestUserServiceException(String message) {
            super(message);
        }
    }

}
