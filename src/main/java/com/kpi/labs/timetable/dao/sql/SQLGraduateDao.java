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

import com.kpi.labs.timetable.dao.GraduateDAO;
import com.kpi.labs.timetable.domain.Graduate;

@Repository
public class SQLGraduateDao implements GraduateDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLGraduateDao.class);
    private static final String INSERT_GRADUATE_SQL =
            "INSERT INTO \"GRADUATE\" (LESSON_ID, USER_ID, LECTURER_ID, GRADUATE_NAME, GRADUATE_SCORE) VALUES (?,?,?,?,?)";
    private static final String LOAD_GRADUATE_SQL = "SELECT GRADUATE_ID FROM \"GRADUATE\" WHERE GRADUATE_NAME=?";
    private static final String LOAD_GRADUATE_FOR_LESSON_AND_USER = "SELECT * FROM \"GRADUATE\" WHERE LESSON_ID=? AND USER_ID=?";
    private static final String LOAD_GRADUATE_FOR_USER = "SELECT * FROM \"GRADUATE\" WHERE USER_ID=?";
    @Autowired
    private JdbcOperations jdbc;

    @Override
    public Integer create(Graduate element) {
        jdbc.execute(INSERT_GRADUATE_SQL, (PreparedStatementCallback) ps -> {
            ps.setObject(1, element.getLesson().getId());
            ps.setObject(2, element.getStudent().getId());
            ps.setObject(3, element.getLecturer().getLecturerId());
            ps.setObject(4, element.getName());
            ps.setObject(5, element.getScore());
            ps.execute();
            return null;
        });
        LOG.info("Set new Graduate - {} for User - {} with Lesson - {} and Lecture - {}", element.getName(), element.getLesson().getName(),
                element.getLecturer().getName());
        return jdbc.queryForObject(LOAD_GRADUATE_SQL, new Object[]{ element.getName() }, Integer.class);
    }

    @Override
    public void update(Graduate element) {
        throw new IllegalArgumentException();
    }

    @Override
    public void delete(Graduate element) {
        throw new IllegalArgumentException();
    }

    @Override
    public List<Graduate> loadByStudentId(Integer studentId) {
        return jdbc.query(LOAD_GRADUATE_FOR_USER, new Object[]{ studentId }, (rs, count) -> {
            Graduate graduate = new Graduate();
            graduate.setName(rs.getString("GRADUATE_NAME"));
            graduate.setScore(rs.getString("GRADUATE_SCORE"));
            graduate.setId(rs.getInt("GRADUATE_ID"));
            return graduate;
        });
    }

    @Override
    public List<Graduate> loadByStudentIdAndLessonId(Integer studentId, Integer lessonId) {
        return jdbc.query(LOAD_GRADUATE_FOR_LESSON_AND_USER, new Object[]{ lessonId, studentId }, (rs, count) -> {
            Graduate graduate = new Graduate();
            graduate.setName(rs.getString("GRADUATE_NAME"));
            graduate.setScore(rs.getString("GRADUATE_SCORE"));
            graduate.setId(rs.getInt("GRADUATE_ID"));
            return graduate;
        });
    }

    @Override
    public Graduate load(Integer key) {
        return null;
    }

    @Override
    public List<Graduate> loadAll() {
        return null;
    }

    protected Graduate mapGraduate(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Graduate graduate = new Graduate();
            graduate.setName(rs.getString("GRADUATE_NAME"));
            graduate.setScore(rs.getString("GRADUATE_SCORE"));
            graduate.setId(rs.getInt("GRADUATE_ID"));
            return graduate;
        }
        return null;
    }
}

