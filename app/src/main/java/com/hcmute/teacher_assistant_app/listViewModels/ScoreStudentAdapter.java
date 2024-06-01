package com.hcmute.teacher_assistant_app.listViewModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.Score.ScoreStudentEditActivity;
import com.hcmute.teacher_assistant_app.models.ScoreInfo;

import java.util.ArrayList;

public class ScoreStudentAdapter extends ArrayAdapter<ScoreInfo> {
    private Context context;
    private int resource;
    private ArrayList<ScoreInfo> data;

    // Constructor
    public ScoreStudentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ScoreInfo> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    // Returns the number of items in the list
    @Override
    public int getCount() {
        return data.size();
    }

    // Method to create and return the view for each item in the list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // Initialize views
        TextView tvStudentID = convertView.findViewById(R.id.student_code);
        TextView tvStudentName = convertView.findViewById(R.id.student_full_name);
        TextView tvStudentScore = convertView.findViewById(R.id.student_score);

        // Get ScoreInfo object at the specified position
        ScoreInfo score = data.get(position);

        // Extract data from ScoreInfo object
        String studentID = String.valueOf(score.getStudentID());
        String studentFullName = score.getStudentFullName();
        String studentScore = String.valueOf(score.getScore());

        // Set text and click listener for the edit button
        ImageView editBtn = convertView.findViewById(R.id.button_edit_score_subject);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch ScoreStudentEditActivity when edit button is clicked
                Intent intent = new Intent(context, ScoreStudentEditActivity.class);
                intent.putExtra("score", score);
                context.startActivity(intent);
            }
        });

        // Set text for student ID, name, and score
        tvStudentID.setText(studentID);
        tvStudentName.setText(studentFullName);
        tvStudentScore.setText(studentScore);

        return convertView;
    }
}
