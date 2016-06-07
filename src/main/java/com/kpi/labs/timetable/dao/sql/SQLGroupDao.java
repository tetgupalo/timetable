package com.kpi.labs.timetable.dao.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.CRUD;
import com.kpi.labs.timetable.domain.Group;

@Repository
public class SQLGroupDao implements CRUD<Group, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(SQLGroupDao.class);
    private static final String INSERT_GROUP_SQL = "INSERT INTO \"GROUP\" (GROUP_NAME, AMOUNT_OF_STUDENTS, GROUP_YEAR) VALUES (?,?,?)";
    private static final String LOAD_GROUP_ID = "Sele";

    @Autowired
    private JdbcOperations jdbc;

    @Override
    public Integer create(Group element) {
        jdbc.execute(INSERT_GROUP_SQL, (PreparedStatementCallback<Void>) ps -> {
            ps.setObject(1, element.getName());
            ps.setObject(2, element.getAmountOfStudents());
            ps.setObject(3, element.getYear());
            return null;
        });
        LOG.info("Inserted new Group - {} with students - {}", element.getName(), element.getAmountOfStudents());
        Integer groupId = jdbc.queryForObject(LOAD_GROUP_ID, Integer.class);
        return groupId;
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
        return null;
    }

    @Override
    public List<Group> loadAll() {
        return null;
    }
}

