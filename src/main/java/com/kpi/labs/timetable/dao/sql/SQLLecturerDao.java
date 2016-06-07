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

import com.kpi.labs.timetable.dao.LecturerDAO;
import com.kpi.labs.timetable.domain.Lecturer;
import com.kpi.labs.timetable.domain.UserRole;

@Repository
public class SQLLecturerDao implements LecturerDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLLecturerDao.class);
    private static final String INSERT_LECTURER_SQL = "INSERT INTO \"LECTURER\" (USER_ID, DEGREE) VALUES (?,?)";
    private static final String LOAD_ALL_LECTURERS = "SELECT * FROM \"LECTURER\" l , \"USER\" u WHERE u.USER_ID=l.USER_ID";
    private static final String LOAD_LECTURER = "SELECT * FROM \"LECTURER\" l , \"USER\" u WHERE u.USER_ID=l.USER_ID AND l.LECTURER_ID=?";
    private static final String LOAD_LECTURER_ID = "SELECT LECTURER_ID FROM \"LECTURER\" WHERE USER_ID=?";
    private static final String LOAD_LECTURER_BY_NAME = "SELECT * FROM \"LECTURER\" l , \"USER\" u WHERE u.USER_ID=l.USER_ID AND l"
            + ".USER_ID=?";

    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private SQLUserDao userDao;

    @Override
    public Integer create(Lecturer element) {
        Integer userId = userDao.create(element);
        jdbc.execute(INSERT_LECTURER_SQL, (PreparedStatementCallback) ps -> {
            ps.setObject(1, userId);
            ps.setObject(2, element.getDegree());
            ps.execute();
            return null;
        });
        Integer lecturerId = jdbc.queryForObject(LOAD_LECTURER_ID, new Object[]{ userId }, Integer.class);
        LOG.info("Inserted new Lecturer - {} with degree - {}", element.getName(), element.getDegree());
        return lecturerId;
    }

    @Override
    public void update(Lecturer element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Lecturer element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Lecturer load(Integer key) {
        return jdbc.query(LOAD_LECTURER, new Object[]{ key }, this::mapLecturer);
    }

    @Override
    public List<Lecturer> loadAll() {
        return jdbc.query(LOAD_ALL_LECTURERS, new Object[]{}, (rs, count) -> {
            return mapLecturer(rs);
        });
    }

    @Override
    public Lecturer loadLecturerByName(String name) {
        Integer userId = userDao.loadUserIdByName(name);
        return jdbc.query(LOAD_LECTURER_BY_NAME, new Object[]{ userId }, rs -> {
            return mapLecturer(rs);
        });
    }

    private Lecturer mapLecturer(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Lecturer lecturer = new Lecturer();
            lecturer.setName(rs.getString("USER_NAME"));
            lecturer.setRole(UserRole.valueOf(rs.getString("Role")));
            lecturer.setId(rs.getInt("USER_ID"));
            lecturer.setLecturerId(rs.getInt("LECTURER_ID"));
            lecturer.setDegree(rs.getString("DEGREE"));
            return lecturer;
        }
        return null;
    }
}

