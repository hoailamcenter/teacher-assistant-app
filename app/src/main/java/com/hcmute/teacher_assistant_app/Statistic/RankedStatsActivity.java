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
import com.hcmute.teacher_assistant_app.DB.ScoreDBHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.ReportScore;
import com.hcmute.teacher_assistant_app.models.ReportTotal;
import com.hcmute.teacher_assistant_app.models.Statistic;

import java.util.ArrayList;
import java.util.List;

public class RankedStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private AnyChartView anyChartView;
    private Alert alert;
    private ScoreDBHelper db = new ScoreDBHelper(this);
    private ArrayList<ReportTotal> reportTotals = new ArrayList<>();
    private String ranked[] = new String[]{"Giỏi", "Khá", "Trung bình", "Yếu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("detail");
        this.alert = new Alert(RankedStatsActivity.this);
        this.alert.normal();

        this.setControl();
        this.setData();
        this.setEvent();

        this.setupPieChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);

        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        title.setText("Thống kê " + statistic.getTitle());
    }

    private void setEvent() {
    }

    private void setupPieChart() {
        List<DataEntry> dataEntries = new ArrayList<>();
        ArrayList<ReportScore> reportScores = db.getReportScore();

        // Iterate over the predefined performance levels (e.g., Excellent, Good, Average, Poor)
        for (int i = 0; i < ranked.length; i++) {
            int count = 0;
            // Count the number of students falling into each performance level
            for (int j = 0; j < reportScores.size(); j++) {
                String pointRank = getXepLoai(reportScores.get(j).getDiem());
                if (pointRank.equals(ranked[i])) {
                    count++;
                }
            }

            // Add the count of students for each performance level to the data entries
            reportTotals.add(new ReportTotal(ranked[i], Double.valueOf(count)));
            dataEntries.add(new ValueDataEntry(ranked[i], count));
        }

        // Create a pie chart using the AnyChart library
        Pie pie = AnyChart.pie();
        pie.data(dataEntries);
        pie.palette(new String[]{"#61CDBB", "#E8A838", "#DC143C", "#473F97"});
        pie.title(statistic.getTitle());
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        // Display the pie chart in the AnyChartView
        anyChartView.setChart(pie);
    }

    // Determine the performance level based on the score
    private String getXepLoai(Double diem) {
        if (diem >= 8) {
            return "Giỏi";
        } else if (diem >= 6.5 && diem < 8) {
            return "Khá";
        } else if (diem >= 5 && diem < 6.5) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}