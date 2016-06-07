package com.kpi.labs.timetable.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kpi.labs.timetable.dao.GraduateDAO;
import com.kpi.labs.timetable.dao.GroupDAO;
import com.kpi.labs.timetable.dao.LecturerDAO;
import com.kpi.labs.timetable.dao.LessonDAO;
import com.kpi.labs.timetable.dao.StudentDAO;
import com.kpi.labs.timetable.domain.Graduate;
import com.kpi.labs.timetable.domain.Group;
import com.kpi.labs.timetable.domain.Lecturer;
import com.kpi.labs.timetable.domain.Lesson;
import com.kpi.labs.timetable.domain.Student;

@Service
public class SQLTimeTableServiceImpl implements TimeTableService {
    private static final Logger LOG = LoggerFactory.getLogger(TimeTableService.class);
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private GraduateDAO graduateDAO;
    @Autowired
    private LessonDAO lessonDAO;
    @Autowired
    private LecturerDAO lecturerDAO;

    @Override
    @Transactional
    public void createStudent(Student student) {
        Group group = student.getGroup();
        List<Lesson> lessons = student.getLessons();
        group.setLessons(lessons);
        Integer groupId = groupDAO.create(group);
        group.setId(groupId);
        for (Lesson lesson : lessons) {
            Lecturer lecturer = lesson.getLecturer();
            Integer lecturerId = lecturerDAO.create(lecturer);
            lecturer.setLecturerId(lecturerId);
            Integer lessonId = lessonDAO.create(lesson);
            lesson.setId(lessonId);
            lesson.setLecturer(lecturer);
            lessonDAO.asigneLesson2Group(lesson, group);
        }
        Integer studentID = studentDAO.create(student);
        student.setId(studentID);
        for (Graduate graduate : student.getGraduates()) {
            graduate.setStudent(student);
            graduate.setLesson(lessonDAO.loadLessonByName(graduate.getLesson().getName()));
            graduate.setLecturer(lecturerDAO.loadLecturerByName(graduate.getLecturer().getName()));
            graduateDAO.create(graduate);
        }
    }

    @Override
    public List<Student> loadStudents() {
        return studentDAO.loadAll();
    }
}

