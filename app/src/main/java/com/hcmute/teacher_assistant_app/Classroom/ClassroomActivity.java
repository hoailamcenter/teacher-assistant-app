package com.hcmute.teacher_assistant_app.Classroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.hcmute.teacher_assistant_app.DB.GradeOpenHelper;
import com.hcmute.teacher_assistant_app.DB.StudentOpenHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.ClassroomListViewModel;
import com.hcmute.teacher_assistant_app.models.Grade;
import com.hcmute.teacher_assistant_app.models.Session;
import com.hcmute.teacher_assistant_app.models.Student;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
//Nguyen Dinh Minh Chien-21110756
public class ClassroomActivity extends AppCompatActivity {
    public static WeakReference<ClassroomActivity> weakActivity;
    private ClassroomListViewModel listViewModel;
    private ListView listView;
    private ArrayList<Student> students = new ArrayList<>();
    private final GradeOpenHelper gradeOpenHelper = new GradeOpenHelper(this);
    private final StudentOpenHelper studentOpenHelper = new StudentOpenHelper(this);
    private AppCompatButton buttonCreation;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        weakActivity = new WeakReference<>(ClassroomActivity.this);// Store weak reference to the activity
        Session session = new Session(ClassroomActivity.this); // Session management

        // get all grades
        ArrayList<Grade> gradeObjects = gradeOpenHelper.retrieveAllGrades();
        // get all students
        this.students = studentOpenHelper.retrieveAllStudents();
        setControl(); // Initialize UI components

        setEvent(); // Set up event handlers
        searchByKeyword(); // Initialize search functionality

        String teacherId = session.get("teacherId");
        String value = gradeOpenHelper.retriveIdByTeachId(teacherId);
        session.set("gradeId", value);
    }

    public static ClassroomActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    private void setControl() {
        // Initialize UI components by finding them by ID
        this.listView = findViewById(R.id.classroomListView);
        this.buttonCreation = findViewById(R.id.classroomButtonCreation);
        this.searchView = findViewById(R.id.classroomSearchView);
    }

    private void setEvent() {
        // Set up the adapter and event handlers for the list view and button
        /*Step 1*/
        this.listViewModel = new ClassroomListViewModel(this, R.layout.activity_classroom_element, students);
        this.listView.setAdapter(listViewModel);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student student = (Student) adapterView.getAdapter().getItem(position);
                // Start ClassroomIndividualActivity when an item is clicked, passing the student object
                Intent intent = new Intent(ClassroomActivity.this, ClassroomIndividualActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        /*Step 2*/
        this.buttonCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassroomActivity.this, ClassroomCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createStudent(Student student) {
        /* Temporary Solution so as to the Grade Name is null */
        student.setGradeName(this.students.get(0).getGradeName());
        // Add the new student to the list and database
        this.students.add(student);

        this.studentOpenHelper.create(student);

        this.listViewModel.notifyDataSetChanged();

        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_LONG).show();
    }

    public void deleteStudent(Student student) {
        if (student.getId() == 0) {
            Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < this.students.size(); i++) {
            if (this.students.get(i).getId() == student.getId()) {
                this.students.remove(this.students.get(i));
            }
        }

        this.listViewModel.notifyDataSetChanged();

        this.studentOpenHelper.delete(student);

        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
    }

    public void updateStudent(Student student) {
        if (student.getId() == 0) {
            Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_LONG).show();
            return;
        }

        // update
        for (Student element : this.students) {
            if (element.getId() == student.getId()) {
                element.setFamilyName(student.getFamilyName());
                element.setFirstName(student.getFirstName());
                element.setGender(student.getGender());
                element.setBirthday(student.getBirthday());
                element.setGradeId(student.getGradeId());
                element.setGradeName(student.getGradeName());
            }
        }

        // re-render
        this.listViewModel.notifyDataSetChanged();
        this.studentOpenHelper.update(student);

        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
    }

    private void searchByKeyword() {
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Student> filteredStudents = new ArrayList<>();

                for (Student student : students) {
                    String firstName = student.getFirstName().toLowerCase(Locale.ROOT);
                    String keyword = s.toLowerCase(Locale.ROOT);

                    if (firstName.contains(keyword)) {
                        filteredStudents.add(student);
                    }
                }

                setListView(filteredStudents);

                return false;
            }
        });
    }

    private void setListView(ArrayList<Student> array) {
        this.listViewModel = new ClassroomListViewModel(this, R.layout.activity_classroom_element, array);
        this.listView.setAdapter(listViewModel);
        this.listViewModel.notifyDataSetChanged();
    }
}