package com.hcmute.teacher_assistant_app.Statistic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.hcmute.teacher_assistant_app.DB.StudentOpenHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.ReportTotal;
import com.hcmute.teacher_assistant_app.models.Statistic;

import java.util.ArrayList;
import java.util.List;
//Nguyễn Hoài Lâm_21110778
public class GenderStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private AnyChartView anyChartView;
    private StudentOpenHelper studentOpenHelper = new StudentOpenHelper(this);
    private ArrayList<ReportTotal> reportTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("detail");

        this.setControl();
        this.setData();
        this.setupPieChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);
        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        this.title.setText("Thống kê " + statistic.getTitle());
    }

    private void setupPieChart() {
        // Retrieve gender statistics from the database
        this.reportTotals = this.studentOpenHelper.countByGender();

        // Prepare data for the pie chart
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < this.reportTotals.size(); i++) {
            data.add(new ValueDataEntry(this.reportTotals.get(i).getName(), this.reportTotals.get(i).getValue()));
        }

        // Create a pie chart using AnyChart library
        Pie pie = AnyChart.pie();

        // Set data to the pie chart
        pie.data(data);

        // Customize pie chart appearance
        pie.palette(new String[]{"#ffcc80", "#aed581"});
        pie.title(this.statistic.getTitle());
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        // Display the pie chart in AnyChartView
        this.anyChartView.setChart(pie);
    }
}