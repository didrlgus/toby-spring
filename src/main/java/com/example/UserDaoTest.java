package com.example;

import com.example.dao.DConnectionMaker;
import com.example.dao.UserDao;
import com.example.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserDao dao = new UserDao(new DConnectionMaker());

        User user = new User();
        user.setId("wade");
        user.setName("yang");
        user.setPassword("abcd1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        System.out.println("name: " + user2.getName());
        System.out.println("password: " + user2.getPassword());
        System.out.println(user.getId() + " 조회 성공");
    }

}
