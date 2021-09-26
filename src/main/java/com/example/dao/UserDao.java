package com.example.dao;

import com.example.domain.User;
import java.util.List;

public interface UserDao {


    void add(User user);

    User get(String id);

    void deleteAll();

    int getCount();

    void update(User user1);

    List<User> getAll();

}
