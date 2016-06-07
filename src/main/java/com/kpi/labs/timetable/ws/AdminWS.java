package com.kpi.labs.timetable.ws;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kpi.labs.timetable.domain.Student;
import com.kpi.labs.timetable.services.TimeTableService;

@RestController
@RequestMapping("/admin/")
public class AdminWS {
    @Autowired
    private TimeTableService timeTableService;

    @RequestMapping(value = "student/add", method = RequestMethod.POST)
    public void addStudent(@RequestBody Student student) {
        timeTableService.createStudent(student);
    }
}

