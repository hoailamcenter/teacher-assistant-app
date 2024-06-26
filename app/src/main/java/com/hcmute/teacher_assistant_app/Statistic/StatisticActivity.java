package com.hcmute.teacher_assistant_app.Statistic;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.StatisticListViewModel;
import com.hcmute.teacher_assistant_app.models.Statistic;

import java.util.ArrayList;
//Nguyễn Hoài Lâm_21110778
public class StatisticActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Statistic> statistics = new ArrayList<>();
    private StatisticListViewModel listViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setControl();
        setEvent();
    }

    private void setControl() {
        this.listView = findViewById(R.id.lvListStatistic);
    }

    private void setEvent() {
        this.statistics.add(new Statistic(1, "Xếp loại", "Xem thống kê một lớp ở học kỳ nhất định có bao nhiêu học sinh giỏi, khá,... theo bảng điểm đã có "));
        this.statistics.add(new Statistic(2, "Phổ điểm tổng kết", "Xem phổ điểm của lớp học nào đó trong học kỳ nhất định"));
        this.statistics.add(new Statistic(3, "Giới tính", "Thống kê giới tính của lớp theo từng học kỳ "));

        this.listViewModel = new StatisticListViewModel(this, R.layout.activity_statistic_row, this.statistics);
        this.listView.setAdapter(listViewModel);
    }
}