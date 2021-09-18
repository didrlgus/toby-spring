package com.example;

import com.example.configuration.DaoFactory;
import com.example.dao.CountingConnectionMaker;
import com.example.dao.DConnectionMaker;
import com.example.dao.UserDao;
import com.example.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.Arrays;

@Slf4j
public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("wade2");
        user.setName("yang");
        user.setPassword("abcd1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        System.out.println("name: " + user2.getName());
        System.out.println("password: " + user2.getPassword());
        System.out.println(user.getId() + " 조회 성공");

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter: " + ccm.getCounter());
    }

}
