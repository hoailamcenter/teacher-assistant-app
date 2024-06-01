package com.hcmute.teacher_assistant_app.Score;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.hcmute.teacher_assistant_app.DB.GradeOpenHelper;
import com.hcmute.teacher_assistant_app.DB.ScoreDBHelper;
import com.hcmute.teacher_assistant_app.DB.StudentOpenHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.ScoreStudentAdapter;
import com.hcmute.teacher_assistant_app.models.Score;
import com.hcmute.teacher_assistant_app.models.ScoreInfo;
import com.hcmute.teacher_assistant_app.models.Session;
import com.hcmute.teacher_assistant_app.models.Student;
import com.hcmute.teacher_assistant_app.models.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreStudentActivity extends AppCompatActivity {
    Session session;
    private int pageHeight = 1120;
    private int pagewidth = 792;

    private ListView listView;
    private Subject subject;
    private ArrayList<Score> objects = new ArrayList<>();
    private ArrayList<ScoreInfo> scoreInfoArrayList = new ArrayList<>();
    private ScoreStudentAdapter listViewModel;

    private GradeOpenHelper gradeOpenHelper = new GradeOpenHelper(this);
    private ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
    private StudentOpenHelper studentDB = new StudentOpenHelper(this);

    private TextView tvSubject;
    private SearchView searchView;
    private String teacher;
    private String grade;
    private String gradeName;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_student);

        session = new Session(ScoreStudentActivity.this);
        teacher = session.get("teacherId");

        grade = gradeOpenHelper.retriveIdByTeachId(teacher);
        gradeName = gradeOpenHelper.retrieveNameById(Integer.parseInt(grade));

        subject = (Subject) getIntent().getSerializableExtra("subject");

        objects = scoreDBHelper.getAll();

        setControl();
        setEvent();
    }

    @SuppressLint("SetTextI18n")
    private void setControl() {
        this.listView = findViewById(R.id.score_student_list_view);
        this.searchView = findViewById(R.id.score_student_search_bar);
        this.tvSubject = findViewById(R.id.score_student_subject_name);

        this.tvSubject.setText("Môn học: " + subject.getTenMH());
    }

    private void setEvent() {
        addStudentScore();

        try {
            scoreInfoArrayList = getData();
            listViewModel = new ScoreStudentAdapter(this, R.layout.activity_score_student_element, scoreInfoArrayList);
            listView.setAdapter(listViewModel);
        } catch (NullPointerException ex) {
            finish();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ScoreStudentActivity.this.listViewModel.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ScoreInfo> filtered = new ArrayList<>();
                for (ScoreInfo score : scoreInfoArrayList) {
                    if (score.getStudentFullName().toLowerCase().trim().contains(newText.toLowerCase().trim()) || String.valueOf(score.getStudentID()).toLowerCase().trim().contains(newText.toLowerCase().trim())) {
                        filtered.add(score);
                    }
                }

                filteredScore(filtered);
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // nếu một hs nào đó chưa có điểm của môn học này thì thêm điểm 0 vào db
    private void addStudentScore() {
        ArrayList<Student> students = studentDB.getStudentInGrade(grade);

        for (Student student : students) {
            ArrayList<Score> scoresByStudentIdAndSubjectId = scoreDBHelper.getStudentAndSubject(String.valueOf(student.getId()), String.valueOf(subject.getMaMH()));

            if (scoresByStudentIdAndSubjectId.size() > 0) continue;

            Score score = new Score(student.getId(), subject.getMaMH(), 0);
            scoreDBHelper.add(score);
        }
    }

    private ArrayList<ScoreInfo> getData() {
        ArrayList<ScoreInfo> scoreInfoArrayList = new ArrayList<>();
        ArrayList<Student> students = studentDB.getStudentInGrade(grade);

        for (Student student : students) {
            ArrayList<Score> scoresByStudentIdAndSubjectId = scoreDBHelper.getStudentAndSubject(String.valueOf(student.getId()), String.valueOf(subject.getMaMH()));
            if (scoresByStudentIdAndSubjectId.size() <= 0) {
                return null;
            }

            // lấy điểm đầu tiên
            Score i = scoresByStudentIdAndSubjectId.get(0);
            ScoreInfo sci = new ScoreInfo(i.getMaHS(), i.getMaMH(), i.getDiem(), student.getFamilyName() + " " + student.getFirstName(), subject.getTenMH());
            scoreInfoArrayList.add(sci);
        }
        return scoreInfoArrayList;
    }


    private void filteredScore(ArrayList<ScoreInfo> filtered) {
        listViewModel = new ScoreStudentAdapter(this, R.layout.activity_score_student_element, filtered);
        listView.setAdapter(listViewModel);
    }
}
