package com.hcmute.teacher_assistant_app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hcmute.teacher_assistant_app.DB.TeacherDBHelper;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.Teacher;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, fullName;
    AppCompatButton btnBack, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setControl();
        this.setEvent();
    }

    private void setEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameText = fullName.getText().toString();
                String passwordText = password.getText().toString();

                Teacher newTeacher = new Teacher(868814071, fullNameText, passwordText);

                try {

                    TeacherDBHelper teacherDBHelper = new TeacherDBHelper(RegisterActivity.this);
                    teacherDBHelper.addTeacher(newTeacher);

                    Alert alert = new Alert(RegisterActivity.this);
                    alert.normal();
                    alert.showAlert("Đăng ký thành công", R.drawable.check_icon);

                    alert.btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                } catch (Exception e) {
                    System.out.printf("error");
                }
            }
        });
    }

    private void setControl() {
        this.username = findViewById(R.id.txtUsername);
        this.password = findViewById(R.id.txtPassword);
        this.fullName = findViewById(R.id.txtFullName);
        this.btnBack = findViewById(R.id.btnBack);
        this.btnRegister = findViewById(R.id.btnRegister);
    }
}