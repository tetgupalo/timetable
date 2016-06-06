package com.kpi.labs.timetable.standalone;

import java.util.Arrays;

public abstract class Bootstrapper {
    public static void main(String[] args) {
        String path = args[0];
        String[] properties = Arrays.copyOfRange(args, 1, args.length);
        ContextLoader.loadContext(path, properties);
    }
}
