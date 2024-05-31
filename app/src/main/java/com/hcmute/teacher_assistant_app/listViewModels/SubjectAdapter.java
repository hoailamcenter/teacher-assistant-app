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
import com.hcmute.teacher_assistant_app.Subject.SubjectActivity;
import com.hcmute.teacher_assistant_app.Subject.SubjectEditActivity;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.Subject;

import java.util.ArrayList;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    private ImageView btn_Edit;
    private ImageView btn_Delete;
    private Subject subject;
    private Context context;
    private int resource;
    private ArrayList<Subject> data;

    public SubjectAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Subject> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        this.btn_Edit = convertView.findViewById(R.id.btn_edit);
        this.btn_Delete = convertView.findViewById(R.id.btn_delete);

        TextView name = convertView.findViewById(R.id.subjectName);
        TextView NKHK = convertView.findViewById(R.id.subjectNKHK);
        TextView heSo = convertView.findViewById(R.id.subjectHS);

        this.subject = this.data.get(position);
        String subject_name = this.subject.getTenMH();
        String subject_NKHK = "Học kỳ: " + this.subject.getHocKy() + " Năm học: " + this.subject.getNamHoc();
        String subject_hs = "Hệ số: " + this.subject.getHeSo();

        this.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject subject1 = data.get(position);
                Intent intent = new Intent(context, SubjectEditActivity.class);
                intent.putExtra("Subject", subject1);
                context.startActivity(intent);
            }
        });

        this.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject subject1 = data.get(position);
                Alert alert = new Alert(context);
                alert.confirm();
                alert.showAlert("Bạn có muốn xóa môn:\n" + subject1.getTenMH(), R.drawable.info_icon);

                alert.btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SubjectActivity.getmInstanceActivity().delSubject(subject1);
                        alert.dismiss();
                    }
                });

                alert.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

            }
        });

        name.setText(subject_name);
        NKHK.setText(subject_NKHK);
        heSo.setText(subject_hs);

        return convertView;
    }

    public void setEvent() {
    }
}
