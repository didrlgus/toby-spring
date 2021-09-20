package com.example.dao;

import com.example.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class UserDao {

    private final JdbcContext jdbcContext;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcContext jdbcContext, DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcContext = jdbcContext;
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) throws SQLException {

//        jdbcContext.workWithStatementStrategy(c -> {
//            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getName());
//            ps.setString(3, user.getPassword());
//
//            return ps;
//        });

        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (nonNull(rs)) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (nonNull(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (nonNull(c)) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }

        if (isNull(user)) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException {

        this.jdbcTemplate.update("delete from users");
    }


    public int getCount() throws SQLException {

//        Connection c = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            c = dataSource.getConnection();
//
//            ps = c.prepareStatement("select count(*) from users");
//
//            rs = ps.executeQuery();
//            rs.next();
//
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (nonNull(rs)) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (nonNull(ps)) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (nonNull(c)) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//                }
//            }
//        }

        return this.jdbcTemplate.query(
                con -> con.prepareStatement("select count(*) from users"),
                rs -> {
                    rs.next();
                    return rs.getInt(1);
                });
    }

}
