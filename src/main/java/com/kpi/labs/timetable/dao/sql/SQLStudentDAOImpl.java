package com.kpi.labs.timetable.dao.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.StudentDAO;
import com.kpi.labs.timetable.domain.Student;

@Repository
public class SQLStudentDAOImpl implements StudentDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLStudentDAOImpl.class);
    private static final String INSERT_USER_SQL = "INSERT INTO \"USER\" (USER_NAME, PASSWORD, ROLE) VALUES (?,?,?)";

    @Autowired
    private JdbcOperations jdbc;

    @Override
    public void create(Student element) {
        jdbc.execute(INSERT_USER_SQL, (PreparedStatementCallback) ps -> {
            ps.setObject(1, element.getName());
            ps.setObject(2, element.getPassword());
            ps.setObject(3, element.getRole().name());
            ps.execute();
            return null;
        });
    }

    @Override
    public void update(Student element) {

    }

    @Override
    public void delete(Student element) {

    }

    @Override
    public Student load(Integer key) {
        return jdbc.query(
                "SELECT * FROM \"USER\" u, \"GROUP\" g  WHERE g.GROUP_ID=u.GROUP_ID AND u.USER_ID=?",
                new Object[]{
                        key
                }, rm -> {
                    return null;
                });
    }

    @Override
    public List<Student> loadAll() {
        return jdbc.query(
                "SELECT * FROM \"USER\" u",
                new Object[]{}, (RowMapper) (rs, lines) -> {
                    Student student = new Student();
                    student.setName(rs.getString("USER_NAME"));
                    return student;
                });
    }

    public JdbcOperations getJdbc() {
        return jdbc;
    }

    public void setJdbc(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }
}

