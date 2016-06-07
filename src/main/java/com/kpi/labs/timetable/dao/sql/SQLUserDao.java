package com.kpi.labs.timetable.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.CRUD;
import com.kpi.labs.timetable.domain.User;
import com.kpi.labs.timetable.domain.UserRole;

@Repository
public class SQLUserDao implements CRUD<User, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(SQLUserDao.class);
    private static final String INSERT_USER_SQL = "INSERT INTO \"USER\" (USER_NAME, ROLE) VALUES (?,?)";
    private static final String LOAD_USER_SQL = "SELECT * FROM \"USER\" WHERE USER_ID=?";
    private static final String LOAD_ALL_USERS_SQL = "SELECT * FROM \"USER\"";
    private static final String LOAD_USER_ID = "SELECT USER_ID FROM \"USER\" WHERE USER_NAME=?";

    @Autowired
    private JdbcOperations jdbc;

    @Override
    public Integer create(User element) {
        jdbc.execute(INSERT_USER_SQL, (PreparedStatementCallback) ps -> {
            ps.setObject(1, element.getName());
            ps.setObject(2, element.getRole().name());
            ps.execute();
            return null;
        });
        LOG.info("Insert new User - {} with role - {}", element.getName(), element.getRole().name());
        return loadUserIdByName(element.getName());
    }

    @Override
    public void update(User element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(User element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User load(Integer key) {
        return jdbc.query(LOAD_USER_SQL, new Object[]{ key }, this::mapUser);
    }

    @Override
    public List<User> loadAll() {
        return jdbc.query(LOAD_ALL_USERS_SQL, new Object[]{}, (rs, count) -> {
            return mapUser(rs);
        });
    }

    protected Integer loadUserIdByName(String name) {
        return jdbc.queryForObject(LOAD_USER_ID, new Object[]{ name }, Integer.class);
    }

    protected User mapUser(ResultSet rs) throws SQLException {
        if (rs.next()) {
            User user = new User();
            user.setName(rs.getString("USER_NAME"));
            user.setRole(UserRole.valueOf(rs.getString("Role")));
            user.setId(rs.getInt("USER_ID"));
            return user;
        }
        return null;
    }
}

