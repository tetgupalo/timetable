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

import com.kpi.labs.timetable.dao.GroupDAO;
import com.kpi.labs.timetable.dao.LessonDAO;
import com.kpi.labs.timetable.dao.StudentDAO;
import com.kpi.labs.timetable.domain.Group;

@Repository
public class SQLGroupDao implements GroupDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLGroupDao.class);
    private static final String INSERT_GROUP = "INSERT INTO \"GROUP\" (GROUP_NAME, AMOUNT_OF_STUDENTS, GROUP_YEAR) VALUES (?,?,?)";
    private static final String LOAD_GROUP_ID = "SELECT GROUP_ID FROM \"GROUP\" WHERE GROUP_NAME=?";
    private static final String LOAD_GROUP_BY_ID = "SELECT * FROM \"GROUP\" WHERE GROUP_ID=?";
    private static final String LOAD_ALL_GROUPS = "SELECT * FROM \"GROUP\"";

    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private LessonDAO lessonDAO;
    @Autowired
    private StudentDAO studentDAO;

    @Override
    public Integer create(Group element) {
        jdbc.execute(INSERT_GROUP, (PreparedStatementCallback<Void>) ps -> {
            ps.setObject(1, element.getName());
            ps.setObject(2, element.getAmountOfStudents());
            ps.setObject(3, element.getYear());
            ps.execute();
            return null;
        });
        LOG.info("Inserted new Group - {} with students - {}", element.getName(), element.getAmountOfStudents());
        return jdbc.queryForObject(LOAD_GROUP_ID, new Object[]{ element.getName() }, Integer.class);
    }

    @Override
    public void update(Group element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Group element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Group load(Integer key) {
        Group group = jdbc.query(LOAD_GROUP_BY_ID, new Object[]{ key }, this::mapGroup);
        group.setLessons(lessonDAO.loadLessonsForGroup(key));
        return group;
    }

    @Override
    public List<Group> loadAll() {
        return jdbc.query(LOAD_ALL_GROUPS, new Object[]{}, (rs, count) -> {
            Group group = mapGroup(rs);
            Integer key = group.getId();
            group.setLessons(lessonDAO.loadLessonsForGroup(key));
            group.setStudents(studentDAO.loadStudentsForGroup(group));
            return group;
        });
    }

    protected Group mapGroup(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Group group = new Group();
            group.setId(rs.getInt("GROUP_ID"));
            group.setName(rs.getString("GROUP_NAME"));
            group.setYear(rs.getInt("GROUP_YEAR"));
            group.setAmountOfStudents(rs.getInt("AMOUNT_OF_STUDENTS"));
            return group;
        }
        return null;
    }
}

