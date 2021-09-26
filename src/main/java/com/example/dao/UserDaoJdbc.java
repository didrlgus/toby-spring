package com.example.dao;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.example.domain.Level;
import com.example.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoJdbc implements UserDao {

    private final JdbcContext jdbcContext;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(JdbcContext jdbcContext, DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcContext = jdbcContext;
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) {

//        jdbcContext.workWithStatementStrategy(c -> {
//            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getName());
//            ps.setString(3, user.getPassword());
//
//            return ps;
//        });

        this.jdbcTemplate
            .update(
                "insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)",
                user.getId(),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
                user.getRecommend());
    }

    public User get(String id) {

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
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
            }
        } catch (SQLException e) {
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

    public void deleteAll() {

        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {

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

    public void update(User user1) {
        this.jdbcTemplate.update(
            "update users set name = ?, password = ?, level = ?, login = ?, " +
                "recommend = ? where id = ?", user1.getName(), user1.getPassword(),
            user1.getLevel().intValue(),
            user1.getLogin(), user1.getRecommend(), user1.getId()
        );
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users", (rs, rowNum)
            -> new User(
            rs.getString("id"), rs.getString("name"),
            rs.getString("password"), Level.valueOf(rs.getInt("level")),
            rs.getInt("login"), rs.getInt("recommend"))
        );
    }

}
