package com.hcmute.teacher_assistant_app.Statistic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.hcmute.teacher_assistant_app.DB.ScoreDBHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.ReportTotal;
import com.hcmute.teacher_assistant_app.models.Statistic;
import com.hcmute.teacher_assistant_app.models.Subject;

import java.util.ArrayList;
import java.util.List;
//Nguyễn Hoài Lâm_21110778
public class MarkStatsActivity extends AppCompatActivity {
    private TextView title;
    private Statistic statistic;
    private Subject subject;
    private AnyChartView anyChartView;

    private ScoreDBHelper db = new ScoreDBHelper(this);
    private ArrayList<ReportTotal> reportTotals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_stats);

        this.statistic = (Statistic) getIntent().getSerializableExtra("Statistic");
        this.subject = (Subject) getIntent().getSerializableExtra("Subject");

        this.setControl();
        this.setData();
        this.setupChart();
    }

    private void setControl() {
        this.title = findViewById(R.id.title);
        this.anyChartView = findViewById(R.id.any_chart_view);
    }

    private void setData() {
        this.title.setText("Thống kê " + statistic.getTitle() + " môn " + subject.getTenMH());
    }

    private void setupChart() {
        // Retrieve report totals for the subject from the database
        this.reportTotals = this.db.getReportCountByScore(this.subject.getMaMH());

        // Prepare data for the column chart
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < this.reportTotals.size(); i++) {
            data.add(new ValueDataEntry(this.reportTotals.get(i).getName(), this.reportTotals.get(i).getValue()));
        }

        // Create a column chart using AnyChart library
        Cartesian cartesian = AnyChart.column();
        Column column = cartesian.column(data);

        // Customize column chart appearance
        column.tooltip().titleFormat("Điểm: {%X}").position(Position.CENTER_BOTTOM).anchor(Anchor.CENTER_BOTTOM).offsetX(0d).offsetY(5d).format("Số lượng: {%Value}");
        cartesian.animation(true);
        cartesian.xAxis(0).title("Điểm");
        cartesian.yAxis(0).title("Số lượng");

        // Display the column chart in AnyChartView
        this.anyChartView.setChart(cartesian);
    }
}