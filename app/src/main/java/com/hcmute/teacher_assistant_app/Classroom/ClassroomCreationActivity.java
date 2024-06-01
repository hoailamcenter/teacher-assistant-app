package com.hcmute.teacher_assistant_app.Classroom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.Session;
import com.hcmute.teacher_assistant_app.models.Student;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
//Nguyen Dinh Minh Chien-21110756
public class ClassroomCreationActivity extends AppCompatActivity {
    private Session session;
    private EditText familyName, firstName, birthday;
    private RadioButton male, female;
    private AppCompatButton buttonConfirm, buttonCancel;
    private ImageButton buttonBirthday;

    private final Calendar cal = Calendar.getInstance();
    private final int currentYear = cal.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_creation);

        this.session = new Session(ClassroomCreationActivity.this);

        setControl();
        setEvent();

        this.birthday.setText("25/10/2003");
    }

    private void setControl() {
        this.buttonConfirm = findViewById(R.id.classroomCreationButtonConfirm);
        this.buttonCancel = findViewById(R.id.classroomCreationButtonGoBack);
        this.familyName = findViewById(R.id.classroomCreationFamilyName);
        this.firstName = findViewById(R.id.classroomCreationFirstName);
        this.male = findViewById(R.id.classroomCreationRadioButtonMale);
        this.female = findViewById(R.id.classroomCreationRadioButtonFemale);
        this.buttonBirthday = findViewById(R.id.classroomCreationButtonBirthday2);
        this.birthday = findViewById(R.id.classroomCreationBirthday);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openDatePicker(View view) {
        // Listener to handle the date selection from the date picker dialog
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;// Adjust month index since it's zero-based in DatePickerDialog
            // Create Date object with selected year, month, and day
            Date date = new Date(year - 1900, month, day);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            // Format the date and set it to the birthday TextView
            String birthdayValue = formatter.format(date);
            birthday.setText(birthdayValue);
        };
        // Get the current year, month, and day from the calendar
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Choose dialog theme based on night mode setting
        int style;
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        } else {
            style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        }
        // Create and show the date picker dialog with the specified theme and current date
        DatePickerDialog datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.show();
    }

    private void setEvent() {
        // If the Android version is Oreo (API 26) or higher, set the onClickListener for the birthday field
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.birthday.setOnClickListener(this::openDatePicker);
        }

        this.buttonConfirm.setOnClickListener(view -> {
            int gender = this.male.isChecked() ? 0 : 1;
            int gradeId = Integer.parseInt(this.session.get("gradeId"));

            Student student = new Student();
            student.setFamilyName(this.familyName.getText().toString());
            student.setFirstName(this.firstName.getText().toString());
            student.setGender(gender);
            student.setGradeId(gradeId);
            student.setBirthday(this.birthday.getText().toString());

            boolean isValid = validateStudentInformation(student);
            if (!isValid) return;

            ClassroomActivity.getmInstanceActivity().createStudent(student);
            finish();
        });

        this.buttonCancel.setOnClickListener(view -> finish());
    }

    //This method checks if the student's family name and first name contain valid characters, and if the student's age is at least 18 years old.
    private boolean validateStudentInformation(Student student) {
        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ" + "áảấẩắẳóỏốổớởíỉýỷéẻếểạậặọộợịỵẹệãẫẵõỗỡĩỹẽễàầằòồờìỳèềaâăoôơiyeêùừụựúứủửũữuư";
        //A regular expression pattern is used to match Vietnamese diacritic characters and Latin letters.
        Pattern pattern = Pattern.compile("(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[a-zA-Z])++");

        boolean flagFamilyName = pattern.matcher(student.getFamilyName()).matches();
        boolean flagFirstName = pattern.matcher(student.getFirstName()).matches();

        if (!flagFamilyName || !flagFirstName) {
            Toast.makeText(ClassroomCreationActivity.this, "Nội dung nhập vào không hợp lệ", Toast.LENGTH_LONG).show();
            return false;
        }

        int yearBirthday = Integer.parseInt(student.getBirthday().substring(6));
        int flagAge = this.currentYear - yearBirthday;
        if (flagAge < 18) {
            Toast.makeText(ClassroomCreationActivity.this, "Tuổi không nhỏ hơn 18", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}