package com.example.dao;

import com.example.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
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

        StatementStrategy st = new DeleteAllStatement();

        jdbcContextWithStatementStrategy(st);
    }

    public int getCount() throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();

            return rs.getInt(1);
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
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
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
    }

}
