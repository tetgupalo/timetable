package com.kpi.labs.timetable.services;

import java.util.List;

import com.kpi.labs.timetable.domain.Student;

public interface TimeTableService {
    void createStudent(Student student);

    List<Student> loadStudents();
}
