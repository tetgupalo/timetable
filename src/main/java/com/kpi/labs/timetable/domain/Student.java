package com.kpi.labs.timetable.domain;

import java.util.List;

public class Student extends User {
    private Group group;
    private List<Graduate> graduates;
    private List<Lesson> lessons;

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

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

