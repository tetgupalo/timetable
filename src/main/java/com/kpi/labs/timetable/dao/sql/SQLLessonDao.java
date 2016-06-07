package com.kpi.labs.timetable.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.LecturerDAO;
import com.kpi.labs.timetable.dao.LessonDAO;
import com.kpi.labs.timetable.domain.Group;
import com.kpi.labs.timetable.domain.Lecturer;
import com.kpi.labs.timetable.domain.Lesson;

@Repository
public class SQLLessonDao implements LessonDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLLessonDao.class);
    private static final String INSERT_LESSON_SQL =
            "INSERT INTO \"LESSON\" (LECTURER_ID, LESSON_NAME, LESSON_TIME, AUDITORY) VALUES (?,?,?,?)";
    private static final String LOAD_LESSON_ID = "SELECT LESSON_ID FROM \"LESSON\" WHERE LESSON_NAME=?";
    private static final String LOAD_LESSONS_FOR_GROUP_SQL =
            "SELECT * FROM \"LESSONS_FOR_GROUPS\" lg, \"LESSON\" l WHERE lg.GROUP_ID=? AND lg.LESSON_ID=l.LESSON_ID";
    private static final String LOAD_ALL_LESSON_SQL = "SELECT * FROM \"LESSON\"";
    private static final String LOAD_LESSON_BY_ID = "SELECT * FROM \"LESSON\" WHERE LESSON_ID=?";
    private static final String LOAD_LESSON_BY_NAME = "SELECT * FROM \"LESSON\" WHERE LESSON_NAME=?";

    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private LecturerDAO lecturerDAO;

    @Override
    public Integer create(Lesson element) {
        jdbc.execute(INSERT_LESSON_SQL, (PreparedStatementCallback) (ps) -> {
            ps.setObject(1, element.getLecturer().getLecturerId());
            ps.setObject(2, element.getName());
            ps.setObject(3, element.getTime());
            ps.setObject(4, element.getAuditory());
            ps.execute();
            return null;
        });
        LOG.info("Insert new Lesson - {} in Auditory - {} with Lecturer - {} ", element.getName(), element.getAuditory(),
                element.getLecturer().getName());
        return jdbc.queryForObject(LOAD_LESSON_ID, new Object[]{ element.getName() }, Integer.class);
    }

    @Override
    public void update(Lesson element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Lesson element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Lesson> loadLessonsForGroup(Integer groupId) {
        return jdbc.query(LOAD_LESSONS_FOR_GROUP_SQL, new Object[]{ groupId }, (rs, rows) ->
                {
                    Lesson lesson = new Lesson();
                    Lecturer lecturer = lecturerDAO.load(rs.getInt("LECTURER_ID"));
                    lesson.setLecturer(lecturer);
                    lesson.setId(rs.getInt("LESSON_ID"));
                    lesson.setName(rs.getString("LESSON_NAME"));
                    lesson.setAuditory(rs.getString("AUDITORY"));
                    lesson.setTime(rs.getString("LESSON_TIME"));
                    return lesson;
                }
        );
    }

    @Override
    public Lesson loadLessonByName(String name) {
        return jdbc.query(LOAD_LESSON_BY_NAME, new Object[]{ name }, rs -> {
            return mapLesson(rs);
        });
    }

    @Override
    public void asigneLesson2Group(Lesson lesson, Group group) {
        jdbc.execute("INSERT INTO \"LESSONS_FOR_GROUPS\" (LESSON_ID, GROUP_ID) VALUES (?,?)", (PreparedStatementCallback) ps -> {
            ps.setObject(1, lesson.getId());
            ps.setObject(2, group.getId());
            ps.execute();
            return null;
        });
    }

    @Override
    public Lesson load(Integer key) {
        return jdbc.query(LOAD_LESSON_BY_ID, new Object[]{ key }, this::mapLesson);
    }


    @Override
    public List<Lesson> loadAll() {
        return jdbc.query(LOAD_ALL_LESSON_SQL, new Object[]{}, (RowMapper) (rs, rows) -> mapLesson(rs));
    }

    protected Lesson mapLesson(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Lesson lesson = new Lesson();
            lesson.setId(rs.getInt("LESSON_ID"));
            lesson.setName(rs.getString("LESSON_NAME"));
            lesson.setAuditory(rs.getString("AUDITORY"));
            lesson.setTime(rs.getString("LESSON_TIME"));
            return lesson;
        }
        return null;
    }
}

