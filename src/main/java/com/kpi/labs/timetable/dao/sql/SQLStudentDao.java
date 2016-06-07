package com.kpi.labs.timetable.dao.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.CRUD;
import com.kpi.labs.timetable.domain.Student;

@Repository
public class SQLStudentDao implements CRUD<Student, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(SQLStudentDao.class);

    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private SQLUserDao userDao;

    @Override
    public Integer create(Student element) {
        Integer userId = userDao.create(element);
        return userId;
    }

    @Override
    public void update(Student element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Student element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student load(Integer key) {
        return null;
    }

    @Override
    public List<Student> loadAll() {
        return null;
    }
}

