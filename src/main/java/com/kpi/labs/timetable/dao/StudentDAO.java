package com.kpi.labs.timetable.dao;

import java.util.List;

import com.kpi.labs.timetable.domain.Group;
import com.kpi.labs.timetable.domain.Student;

public interface StudentDAO extends CRUD<Student, Integer> {
    List<Student> loadStudentsForGroup(Group group);
}
