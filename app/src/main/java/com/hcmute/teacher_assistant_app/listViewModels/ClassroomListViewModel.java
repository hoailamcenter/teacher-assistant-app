package com.hcmute.teacher_assistant_app.listViewModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.Student;

import java.util.ArrayList;

public class ClassroomListViewModel extends ArrayAdapter<Student> {
    private Context context; // The context in which the adapter is used
    private int resource; // The layout resource ID for a list item
    private ArrayList<Student> objects; // The list of Student objects

    // Constructor
    public ClassroomListViewModel(@NonNull Context context, int resource, @NonNull ArrayList<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    // Returns the size of the list
    public int count() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // If convertView is null, inflate a new view with the specified resource
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // Find the TextViews in the layout
        TextView name = convertView.findViewById(R.id.studentName);
        TextView grade = convertView.findViewById(R.id.studentGrade);

        // Get the Student object at the current position
        Student student = objects.get(position);

        // Concatenate the family name and first name to form the full name
        String studentName = student.getFamilyName() + " " + student.getFirstName();
        // Get the grade name
        String studentGrade = student.getGradeName();

        // Set the text for the name and grade TextViews
        name.setText(studentName);
        grade.setText(studentGrade);

        // Return the completed view to render on screen
        return convertView;
    }
}
