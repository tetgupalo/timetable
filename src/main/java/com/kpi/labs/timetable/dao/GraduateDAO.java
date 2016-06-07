package com.kpi.labs.timetable.dao;

import java.util.List;

import com.kpi.labs.timetable.domain.Graduate;

public interface GraduateDAO extends CRUD<Graduate, Integer> {
    List<Graduate> loadByStudentId(Integer studentId);

    List<Graduate> loadByStudentIdAndLessonId(Integer studentId, Integer lessonId);
}
