package com.hcmute.teacher_assistant_app.Score;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.ScoreSubjectAdapter;
import com.hcmute.teacher_assistant_app.models.Subject;
import com.hcmute.teacher_assistant_app.DB.SubjectDBHelper;
import java.util.ArrayList;
//Nguyễn Ngọc Minh_21110784
public class ScoreSubjectActivity extends AppCompatActivity {
    private SearchView scoreSearch;
    private ListView listView;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ScoreSubjectAdapter listViewModel;
    private SubjectDBHelper subjectDB = new SubjectDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_subject);

        subjects = subjectDB.getAllSubjects();

        setControl();
        setEvent();
        inItSearchWidgets();
    }

    private void setControl() {
        scoreSearch = findViewById(R.id.ScoreSearch);
        listView = findViewById(R.id.score_subject_list_view);
    }

    private void setEvent() {
        listViewModel = new ScoreSubjectAdapter(this, R.layout.activity_score_subject_element, subjects);
        listView.setAdapter(listViewModel);
    }

    private void inItSearchWidgets() {
        scoreSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // No action needed on query text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Subject> filteredSubject = new ArrayList<Subject>();// List to hold filtered subjects

                // Filter subjects based on the search query

                for (Subject subject : subjects) {
                    if (subject.getTenMH().toLowerCase().trim().contains(s.toLowerCase().trim())) {
                        filteredSubject.add(subject); // Add matching subjects to the filtered list
                    }
                }

                setFilteredSubject(filteredSubject); // Update ListView with filtered subjects
                return false;
            }
        });
    }

    private void setFilteredSubject(ArrayList<Subject> filtered) {
        // Create a new adapter with filtered subjects and set it to the ListView
        ScoreSubjectAdapter subjectAdapter = new ScoreSubjectAdapter(this, R.layout.activity_score_subject_element, filtered);
        listView.setAdapter(subjectAdapter);
    }
}
