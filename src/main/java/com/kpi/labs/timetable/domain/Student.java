package com.kpi.labs.timetable.domain;

import java.util.List;

public class Student extends User {
    private List<Graduate> graduates;

    public List<Graduate> getGraduates() {
        return graduates;
    }

    public void setGraduates(List<Graduate> graduates) {
        this.graduates = graduates;
    }
}

