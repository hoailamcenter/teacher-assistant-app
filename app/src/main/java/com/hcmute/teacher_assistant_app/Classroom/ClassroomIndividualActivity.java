package com.hcmute.teacher_assistant_app.Classroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.Student;

import java.lang.ref.WeakReference;
//Nguyen Dinh Minh Chien-21110756
public class ClassroomIndividualActivity extends AppCompatActivity {

    public static WeakReference<ClassroomIndividualActivity> weakActivity;

    TextView studentFamilyName, studentFirstName, studentGradeName, studentBirthday, studentGender, contentAlert;
    Button buttonGoBack, buttonUpdate, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_individual);

        this.weakActivity = new WeakReference<>(ClassroomIndividualActivity.this);
        setControl();

        setScreen();
        setEvent();
    }
    // Static method to get an instance of this activity
    public static ClassroomIndividualActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    // Initialize UI controls
    private void setControl() {
        this.studentFamilyName = findViewById(R.id.studentFamilyName);
        this.studentFirstName = findViewById(R.id.studentFirstName);

        this.studentGradeName = findViewById(R.id.gradeName);
        this.studentBirthday = findViewById(R.id.birthday);

        this.buttonGoBack = findViewById(R.id.individualButtonGoBack);
        this.buttonUpdate = findViewById(R.id.individualButtonUpdate);
        this.buttonDelete = findViewById(R.id.individualButtonDelete);

        this.studentGender = findViewById(R.id.gender);
    }

    // Populate screen with student details passed through intent
    private void setScreen() {
        /*Step 1*/
        Student student = (Student) getIntent().getSerializableExtra("student");

        String familyName = student.getFamilyName();
        String firstName = student.getFirstName();
        String birthday = student.getBirthday();
        String gradeName = student.getGradeName();

        /*Step 2*/
        this.studentFamilyName.setText(familyName);
        this.studentFirstName.setText(firstName);
        this.studentBirthday.setText(birthday);
        this.studentGradeName.setText(gradeName);

        if (student.getGender() == 0) {
            this.studentGender.setText("Nam");
        } else {
            this.studentGender.setText("Nữ");
        }
    }

    // Set event listeners for buttons
    private void setEvent() {
        // Get student object from intent
        Student student = (Student) getIntent().getSerializableExtra("student");
        // Set click listener for delete button
        this.buttonDelete.setOnClickListener(view -> triggerPopupWindow(view, student));
        // Set click listener for update button to open update activity
        this.buttonUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(ClassroomIndividualActivity.this, ClassroomUpdateActivity.class);
            intent.putExtra("updatedStudent", student);
            startActivity((intent));
        });
        // Set click listener for go back button to finish the current activity
        this.buttonGoBack.setOnClickListener(view -> {
            finish();
        });
    }


    // Show a confirmation popup window for deleting a student
    @SuppressLint("ClickableViewAccessibility")
    private void triggerPopupWindow(View view, Student student) {
        // Create and configure alert dialog
        Alert alert = new Alert(ClassroomIndividualActivity.this);
        alert.confirm();
        alert.showAlert(R.string.deleteStudent, R.drawable.info_icon);
        // Set click listener for OK button to delete student
        alert.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassroomActivity.getmInstanceActivity().deleteStudent(student);
                finish();
            }
        });
        // Set click listener for Cancel button to dismiss the alert dialog
        alert.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }
    // Update student details displayed on the screen
    public void updateStudent(Student student) {
        this.studentFamilyName.setText(student.getFamilyName());
        this.studentFirstName.setText(student.getFirstName());
        this.studentBirthday.setText(student.getBirthday());
        this.studentGradeName.setText(student.getGradeName());

        if (student.getGender() == 0) {
            this.studentGender.setText("Nam");
        } else {
            this.studentGender.setText("Nữ");
        }
    }
}