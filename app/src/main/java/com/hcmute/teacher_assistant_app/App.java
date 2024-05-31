package com.hcmute.teacher_assistant_app;

import android.app.Application;

import com.hcmute.teacher_assistant_app.models.Teacher;


public class App extends Application {

    private Teacher gv = null;

    public Teacher getTeacher() {
        return gv;
    }

    public void setTeacher(Teacher gv) {
        this.gv = gv;
    }
}
