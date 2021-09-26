package com.example.configuration;

import com.example.dao.*;
import com.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserService userService() {

        return new UserService(userDao());
    }

    @Bean
    public UserDao userDao() {

        return new UserDaoJdbc(jdbcContext(), dataSource(), jdbcTemplate());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(dataSource());
    }

    @Bean
    public JdbcContext jdbcContext() {

        return new JdbcContext(dataSource());
    }

    @Bean
    public DataSource dataSource() {

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3307/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd1234");

        return dataSource;
    }

//    @Bean
//    public ConnectionMaker connectionMaker() {
//
//        return new CountingConnectionMaker(realConnectionMaker());
//    }
//
//    @Bean
//    public ConnectionMaker realConnectionMaker() {
//
//        return new DConnectionMaker();
//    }

}
