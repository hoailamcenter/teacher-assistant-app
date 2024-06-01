package com.hcmute.teacher_assistant_app.listViewModels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.Statistic.GenderStatsActivity;
import com.hcmute.teacher_assistant_app.Statistic.RankedStatsActivity;
import com.hcmute.teacher_assistant_app.Statistic.StatisticActivity;
import com.hcmute.teacher_assistant_app.Statistic.SubjectListActivity;
import com.hcmute.teacher_assistant_app.models.Statistic;

import java.util.ArrayList;

public class StatisticListViewModel extends ArrayAdapter<Statistic> {
    private Context context;
    private int resource;
    private ArrayList<Statistic> statistics;

    // Constructor
    public StatisticListViewModel(@NonNull Context context, int resource, @NonNull ArrayList<Statistic> statistics) {
        super(context, resource, statistics);
        this.context = context;
        this.resource = resource;
        this.statistics = statistics;
    }

    // Method to get the number of items in the list
    public int count() {
        return statistics.size();
    }

    // Method to create and return the view for each item in the list
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // Initialize views
        TextView title = convertView.findViewById(R.id.titleStatistic);
        TextView text = convertView.findViewById(R.id.textStatistic);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);

        // Get Statistic object at the specified position
        Statistic statistic = statistics.get(position);

        // Set text for title and text views
        title.setText(statistic.getTitle());
        text.setText(statistic.getText());

        // Set click listener for the edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle different actions based on the statistic ID
                switch (statistic.getId()) {
                    case 1:
                        Intent intent1 = new Intent(context, RankedStatsActivity.class);
                        intent1.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, SubjectListActivity.class);
                        intent2.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, GenderStatsActivity.class);
                        intent3.putExtra("detail", statistic);
                        ((StatisticActivity) context).startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }
}
