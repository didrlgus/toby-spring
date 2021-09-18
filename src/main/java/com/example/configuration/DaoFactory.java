package com.example.configuration;

import com.example.dao.ConnectionMaker;
import com.example.dao.CountingConnectionMaker;
import com.example.dao.DConnectionMaker;
import com.example.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {

        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {

        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {

        return new DConnectionMaker();
    }

}
