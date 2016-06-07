package com.kpi.labs.timetable.ws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kpi.labs.timetable.domain.Student;
import com.kpi.labs.timetable.services.TimeTableService;

@RestController
@RequestMapping("/student/")
public class StudentWS {
    @Autowired
    private TimeTableService timeTableServiceImpl;

    @RequestMapping(value = "time_table", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> loadTimeTable() {
        return new ResponseEntity<>(timeTableServiceImpl.loadStudents(), HttpStatus.OK);
    }
}

