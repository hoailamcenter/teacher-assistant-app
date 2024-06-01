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
import com.hcmute.teacher_assistant_app.Statistic.MarkStatsActivity;
import com.hcmute.teacher_assistant_app.models.Statistic;
import com.hcmute.teacher_assistant_app.models.Subject;

import java.util.ArrayList;

public class SubjectListAdapter extends ArrayAdapter<Subject> {
    private ImageView btn_Edit;
    private Subject subject;
    private Statistic statistic;
    private Context context;
    private int resource;
    private ArrayList<Subject> subjects;

    // Constructor
    public SubjectListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Subject> subjects, Statistic statistic) {
        super(context, resource, subjects);
        this.context = context;
        this.resource = resource;
        this.subjects = subjects;
        this.statistic = statistic;
    }

    // Get the number of items in the list
    @Override
    public int getCount() {
        return subjects.size();
    }

    // Create and return the view for each item in the list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        btn_Edit = convertView.findViewById(R.id.btn_edit);

        TextView name = convertView.findViewById(R.id.subjectName);
        TextView NKHK = convertView.findViewById(R.id.subjectNKHK);
        TextView heSo = convertView.findViewById(R.id.subjectHS);

        // Get the subject at the specified position
        subject = subjects.get(position);
        String subject_name = subject.getTenMH();
        String subject_NKHK = "Học kỳ: " + subject.getHocKy() + " Năm học: " + subject.getNamHoc();
        String subject_hs = "Hệ số: " + subject.getHeSo();

        // Set text for TextViews
        name.setText(subject_name);
        NKHK.setText(subject_NKHK);
        heSo.setText(subject_hs);

        // Set click listener for the edit button
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject subject = subjects.get(position);

                // Navigate to the MarkStatsActivity with subject and statistic information
                Intent intent = new Intent(context, MarkStatsActivity.class);
                intent.putExtra("Subject", subject);
                intent.putExtra("Statistic", statistic);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
