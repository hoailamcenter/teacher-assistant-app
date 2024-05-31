package com.hcmute.teacher_assistant_app.DB;

public class DBConfig {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "teacher-assistant";

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }
}
