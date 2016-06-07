package com.kpi.labs.timetable.dao;

import com.kpi.labs.timetable.domain.Lecturer;

public interface LecturerDAO extends CRUD<Lecturer, Integer> {
    Lecturer loadLecturerByName(String name);
}
