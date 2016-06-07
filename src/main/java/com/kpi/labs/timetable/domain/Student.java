package com.kpi.labs.timetable.domain;

import java.util.List;

public class Student extends User {
    private List<Graduate> graduates;
    private Group group;

    public List<Graduate> getGraduates() {
        return graduates;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setGraduates(List<Graduate> graduates) {
        this.graduates = graduates;
    }
}

