package com.kpi.labs.timetable.standalone;

public class TimeTableApp {
    public static void main(String[] args) {
        ContextLoader.loadContext("classpath:", new String[]{ "app.properties" });
    }
}
