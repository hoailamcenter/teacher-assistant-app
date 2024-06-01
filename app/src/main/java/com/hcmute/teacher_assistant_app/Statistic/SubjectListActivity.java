package com.hcmute.teacher_assistant_app.Statistic;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.DB.SubjectDBHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.SubjectListAdapter;
import com.hcmute.teacher_assistant_app.models.Statistic;
import com.hcmute.teacher_assistant_app.models.Subject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
//Nguyễn Hoài Lâm_21110778
public class SubjectListActivity extends AppCompatActivity {
    public static WeakReference<SubjectListActivity> weakActivity;
    private Statistic statistic;
    private ListView listView;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private SubjectListAdapter subjectListAdapter;
    private SubjectDBHelper subjectDB = new SubjectDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        weakActivity = new WeakReference<>(SubjectListActivity.this);

        statistic = (Statistic) getIntent().getSerializableExtra("detail");

        subjects = subjectDB.getAllSubjects();

        setControl();
        setEvent();
        inItSearchWidgets();
    }

    private void setControl() {
        this.listView = findViewById(R.id.subjectListView);
    }

    private void setEvent() {
        // Initialize the SubjectListAdapter and set it to the ListView
        this.subjectListAdapter = new SubjectListAdapter(this, R.layout.statistic_subject_element, subjects, statistic);
        this.listView.setAdapter(this.subjectListAdapter);
    }

    private void inItSearchWidgets() {
        SearchView searchView = findViewById(R.id.searchSubject);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Filter subjects based on the search query and update the ListView
                ArrayList<Subject> filteredSubject = new ArrayList<Subject>();

                for (Subject subject : subjects) {
                    if (subject.getTenMH().toLowerCase().trim().contains(s.toLowerCase().trim())) {
                        filteredSubject.add(subject);
                    }
                }

                setFilteredSubject(filteredSubject);
                return false;
            }
        });
    }

    private void setFilteredSubject(ArrayList<Subject> filtered) {
        // Update the ListView with the filtered list of subjects
        SubjectListAdapter subjectAdapter = new SubjectListAdapter(this, R.layout.statistic_subject_element, filtered, statistic);
        this.listView.setAdapter(subjectAdapter);
    }

    // Method to get the instance of this activity
    public static SubjectListActivity getmInstanceActivity() {
        return weakActivity.get();
    }
}