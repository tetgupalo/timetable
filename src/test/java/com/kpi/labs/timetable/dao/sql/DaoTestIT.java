package com.kpi.labs.timetable.dao.sql;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kpi.labs.timetable.dao.StudentDAO;
import com.kpi.labs.timetable.domain.Student;
import com.kpi.labs.timetable.domain.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:META-INF/spring/config.storage.xml" })
public class DaoTestIT {
    @Autowired
    private StudentDAO studentDAO;

    @Test
    public void insertStudent() throws Exception {
        System.out.println(new DateTime());
        Student student = new Student();
        student.setName("123");
        student.setPassword("123");
        student.setRole(UserRole.STUDENT);
        studentDAO.create(student);
        List<Student> students = studentDAO.loadAll();
        for (Student s : students) {
            System.out.println(s.getName());
        }
    }
}
