package com.hcmute.teacher_assistant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hcmute.teacher_assistant_app.DB.EventDBHelper;
import com.hcmute.teacher_assistant_app.DB.GradeOpenHelper;
import com.hcmute.teacher_assistant_app.DB.ScoreDBHelper;
import com.hcmute.teacher_assistant_app.DB.StudentOpenHelper;
import com.hcmute.teacher_assistant_app.DB.SubjectDBHelper;
import com.hcmute.teacher_assistant_app.DB.TeacherDBHelper;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.Session;
import com.hcmute.teacher_assistant_app.models.Teacher;

public class LoginActivity extends AppCompatActivity {
    Session session;
    EditText txtUsername, txtPassword;
    AppCompatButton btnSignIn, btnRegister;
    TeacherDBHelper teacherDBHelper = new TeacherDBHelper(this);
    Boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.session = new Session(LoginActivity.this);

        checkAuth();
        setControl();
        setEvent();
        //createSampleData();
    }

    private void checkAuth() {
        Teacher teacher = ((App) LoginActivity.this.getApplication()).getTeacher();
        if (teacher == null) return;
        gotoHome();
    }

    private void setControl() {
        this.txtUsername = findViewById(R.id.txtUsername);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.btnRegister = findViewById(R.id.btnRegister);
    }

    private void setEvent() {
        this.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting input
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // checking input
                if (username.isEmpty()) {
                    txtUsername.setError("Bạn chưa nhập tên đăng nhập");
                    return;
                } else if (password.isEmpty()) {
                    txtPassword.setError("Bạn chưa nhập mật khẩu");
                    return;
                }

                // initializing alert
                Alert alert = new Alert(LoginActivity.this);
                alert.normal();

                // getting teacher by id (username)
                Teacher teacher = teacherDBHelper.getTeacher(Integer.parseInt(username));

                // checking login information
                if (teacher == null) {
                    isLogin = false;
                    alert.showAlert("Tài khoản không tồn tại!", R.drawable.info_icon);
                } else if (!teacher.getPassword().equals(password)) {
                    isLogin = false;
                    alert.showAlert("Mật khẩu không chính xác", R.drawable.info_icon);
                } else {
                    isLogin = true;
                    // set global variable
                    ((App) LoginActivity.this.getApplication()).setTeacher(teacher);

                    session.set("teacherName", teacher.getName());
                    session.set("teacherId", String.valueOf(teacher.getId()));

                    alert.showAlert("Đăng nhập thành công!", R.drawable.check_icon);
                }

                alert.btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isLogin) {
                            gotoHome();
                        }
                        alert.dismiss();
                    }
                });
            }
        });

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createSampleData() {
        TeacherDBHelper dbDemoTeacher = new TeacherDBHelper(LoginActivity.this);
        dbDemoTeacher.deleteAndCreatTable();

        GradeOpenHelper dbDemoGrade = new GradeOpenHelper(LoginActivity.this);
        dbDemoGrade.deleteAndCreatTable();

        StudentOpenHelper dbDemoStudent = new StudentOpenHelper(LoginActivity.this);
        dbDemoStudent.deleteAndCreateTable();

        SubjectDBHelper dbDemoSubject = new SubjectDBHelper(LoginActivity.this);
        dbDemoSubject.deleteAndCreateTable();

        ScoreDBHelper scoreDBHelper = new ScoreDBHelper(LoginActivity.this);
        scoreDBHelper.deleteAndCreateTable();

        EventDBHelper eventDBHelper = new EventDBHelper(LoginActivity.this);
        eventDBHelper.deletedAndCreateTable();
    }

    public void gotoHome() {
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        LoginActivity.this.startActivity(i);
    }
}