package com.kpi.labs.timetable.dao;

import java.util.List;

import com.kpi.labs.timetable.domain.Group;
import com.kpi.labs.timetable.domain.Lesson;

public interface LessonDAO extends CRUD<Lesson, Integer> {

    void asigneLesson2Group(Lesson lesson, Group group);

    List<Lesson> loadLessonsForGroup(Integer groupId);

    Lesson loadLessonByName(String name);
}
